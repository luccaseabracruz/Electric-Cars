package com.example.eletriccars.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.eletriccars.R
import com.example.eletriccars.data.local.CarRepository
import com.example.eletriccars.domain.Car

class CarAdapter (val context: Context, private val cars: List<Car>): RecyclerView.Adapter<CarAdapter.ViewHolder>() {

    var carItemListener: (Car) -> Unit = {}

    // Cria uma nova view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.car_item, parent, false)

        return ViewHolder(view)
    }


    // Pega o conteúdo da view e troca pela informação de item de uma lista
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val car = cars[position]

        holder.price.text = car.price
        holder.battery.text = car.battery
        holder.horsepower.text = car.horsepower
        holder.recharge.text = car.recharge
        holder.favorite.setOnClickListener {
            carItemListener(car)
            setupFavorite(car, holder)
        }
        setupFavoriteIcon(car, holder)

    }

    private fun setupFavoriteIcon(car: Car, holder: ViewHolder) {
        val repository = CarRepository(context)
        val carSearched = repository.findCarById(car.id)

        if(carSearched.id == 0){
            holder.favorite.setImageResource(R.drawable.ic_star)
        } else {
            holder.favorite.setImageResource(R.drawable.ic_star_selected)
        }
    }


    private fun setupFavorite(
        car: Car,
        holder: ViewHolder
    ) {
        car.isFavorite = !car.isFavorite

        when {
            car.isFavorite -> holder.favorite.setImageResource(R.drawable.ic_star_selected)
            else -> holder.favorite.setImageResource((R.drawable.ic_star))
        }
    }

    // Pega a quantidade de carros na lista
    override fun getItemCount(): Int = cars.size


    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var price: TextView
        var battery: TextView
        var horsepower: TextView
        var recharge: TextView
        var favorite: ImageView

        init {
            view.apply {
                price = findViewById(R.id.tv_price_value)
                battery = findViewById(R.id.tv_batery_value)
                horsepower = findViewById(R.id.tv_horsepower_value)
                recharge = findViewById(R.id.tv_recharge_value)
                favorite = findViewById(R.id.iv_favorite)
            }
        }
    }
}

