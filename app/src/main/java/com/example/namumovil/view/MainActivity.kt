package com.example.namumovil.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.namumovil.R
import com.example.namumovil.databinding.ActivityMainBinding
import com.example.namumovil.fragments.HomeFragment
import com.example.namumovil.fragments.CreateReservationFragment
import com.example.namumovil.fragments.SeeReservationFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            val fragment = when(item.itemId){
                R.id.item_1 -> {
                    HomeFragment()
                }
                R.id.item_2 -> {
                    CreateReservationFragment()
                }
                R.id.item_3 ->{
                    SeeReservationFragment()
                }
                else -> null
            }
            if (fragment != null) {
                supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
            }
            true
        }
    }
}