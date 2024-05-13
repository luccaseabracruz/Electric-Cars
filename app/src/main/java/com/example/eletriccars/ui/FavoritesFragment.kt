package com.example.eletriccars.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.eletriccars.R
import com.example.eletriccars.data.local.CarRepository
import com.example.eletriccars.domain.Car
import com.example.eletriccars.ui.adapter.CarAdapter

class FavoritesFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favorites_fragment, container, false)
    }

    lateinit var favoritesList: RecyclerView


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(view)
    }

    override fun onResume() {
        super.onResume()

        setupList()
    }

    private fun getCarsOnLocalDb(): List<Car> {
        val repository = CarRepository(requireContext())
        return repository.getAllCars()
    }

    fun setupViews(view: View) {
        view.apply {
            favoritesList = findViewById(R.id.rv_favorites_list)
        }
    }

    fun setupList() {
        val dbList = getCarsOnLocalDb()
        val carAdapter = CarAdapter(requireContext(), dbList)
        favoritesList.apply {
            adapter = carAdapter
            visibility = View.VISIBLE
        }

        carAdapter.carItemListener = { car ->
            val isDeleted = CarRepository(requireContext()).delete(car)
            updateList()
        }
    }

    fun updateList() {
        setupList()
    }

}