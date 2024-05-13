package com.example.eletriccars.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.eletriccars.ui.CarsFragment
import com.example.eletriccars.ui.FavoritesFragment

class TabAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> CarsFragment()
            1 -> FavoritesFragment()
            else -> CarsFragment()
        }
    }

    override fun getItemCount(): Int = 2
}