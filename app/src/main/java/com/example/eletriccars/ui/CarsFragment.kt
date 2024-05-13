package com.example.eletriccars.ui


import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.eletriccars.R
import com.example.eletriccars.data.CarsApi
import com.example.eletriccars.data.local.CarRepository
import com.example.eletriccars.domain.Car
import com.example.eletriccars.ui.adapter.CarAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class CarsFragment : Fragment() {
    lateinit var fabCalculateAutonomy: FloatingActionButton
    lateinit var list: RecyclerView
    lateinit var loader: ProgressBar
    lateinit var noInternetImage: ImageView
    lateinit var noInternetText: TextView

    lateinit var carsApi: CarsApi

    var carsList: ArrayList<Car> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.cars_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRetrofit()
        setupViews(view)
        setupListeners()
    }

    override fun onResume() {
        super.onResume()

        if(checkConnectivity(context)){
            //callService()
            getAllCars()
        } else {
            emptyState()
        }
    }

    fun setupRetrofit() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://igorbag.github.io/cars-api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        carsApi = retrofit.create(CarsApi::class.java)
    }

    fun getAllCars() {
        carsApi.getAllCars().enqueue(object: retrofit2.Callback<List<Car>>{
            override fun onResponse(call: Call<List<Car>>, response: Response<List<Car>>) {
                if(response.isSuccessful){
                    loader.isVisible = false
                    noInternetText.isVisible = false
                    noInternetImage.isVisible = false
                    response.body()?.let{
                        setupList(it)
                    }
                } else {
                    Toast.makeText(context, R.string.no_connection_text, Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<List<Car>>, t: Throwable) {
                Toast.makeText(context, R.string.no_connection_text, Toast.LENGTH_LONG).show()
            }

        })
    }

    fun emptyState() {
        fabCalculateAutonomy.isVisible = false
        list.isVisible = false
        loader.isVisible = false
        noInternetImage.isVisible = true
        noInternetText.isVisible = true
    }

    fun setupViews(view: View) {
        view.apply {
            fabCalculateAutonomy = findViewById(R.id.fab_calculate_autonomy)
            list = findViewById(R.id.rv_cars_list)
            loader = findViewById(R.id.pb_loader)
            noInternetImage = findViewById(R.id.iv_no_internet_empty_state)
            noInternetText = findViewById(R.id.tv_no_internet_empty_state)
        }
    }

    fun setupListeners() {
        fabCalculateAutonomy.setOnClickListener() {
          startActivity(Intent(context, CalculateAutonomyActivity::class.java))
        }
    }

    fun setupList(objectList: List<Car>) {
        val carAdapter = CarAdapter(requireContext(), objectList)
        list.apply {
            adapter = carAdapter
            visibility = View.VISIBLE
        }

        carAdapter.carItemListener = { car ->
            val isSaved = CarRepository(requireContext()).saveIfNotExists(car)
        }

        fabCalculateAutonomy.visibility = View.VISIBLE

    }

    fun callService() {
        val url = "https://igorbag.github.io/cars-api/cars.json"
        MyTask().execute(url)
        loader.isVisible = true
    }

    fun checkConnectivity(context: Context?): Boolean {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }



    //AssyncTask foi substituído pelo Retrofit
    inner class MyTask(): AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            //Log.d("MyTask:", "INICIANDO.....")
        }

        override fun doInBackground(vararg url: String?): String {
            var urlConnection: HttpURLConnection? = null

            try {
                val urlBase = URL(url[0])

                urlConnection = urlBase.openConnection() as HttpURLConnection
                urlConnection.connectTimeout = 6000
                urlConnection.readTimeout = 6000
                urlConnection.setRequestProperty(
                    "Accept",
                    "application/json"
                )

                val responseCode = urlConnection.responseCode

                if(responseCode == HttpURLConnection.HTTP_OK){
                    val response = urlConnection.inputStream.bufferedReader().use {it.readText()}
                    publishProgress(response)
                }

            } catch (ex: Exception){
                Log.e("Error","Error while processing")
            } finally {
                urlConnection?.disconnect()
            }

            return " "
        }

        override fun onProgressUpdate(vararg values: String?) {
            try {
                //Esse código diz serve para quando recebe um só objeto e não uma lista inteira

                //var json: JSONObject
                //values[0]?.let {
                //    json = JSONObject(it)
                //}

                val jsonArray = JSONTokener(values[0]).nextValue() as JSONArray

                for (i in 0 until jsonArray.length()){
                    val id = jsonArray.getJSONObject(i).getString("id")
                    val price = jsonArray.getJSONObject(i).getString("preco")
                    val battery = jsonArray.getJSONObject(i).getString("bateria")
                    val horsepower = jsonArray.getJSONObject(i).getString("potencia")
                    val recharge = jsonArray.getJSONObject(i).getString("recarga")
                    val urlPhoto = jsonArray.getJSONObject(i).getString("urlPhoto")

                    val model = Car(
                        id = id.toInt(),
                        price = price,
                        battery = battery,
                        horsepower = horsepower,
                        recharge = recharge,
                        urlPhoto = urlPhoto,
                        isFavorite = false
                    )

                    carsList.add(model)
                }
                loader.isVisible = false
                noInternetText.isVisible = false
                noInternetImage.isVisible = false
                //setupList()

            } catch (ex: Exception){
                Log.e("Error", ex.message.toString())
            }
        }
    }
}