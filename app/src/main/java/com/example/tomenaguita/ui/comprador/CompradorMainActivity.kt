package com.example.tomenaguita.ui.comprador

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.tomenaguita.R
import com.example.tomenaguita.databinding.ActivityCompradorMainBinding

class CompradorMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCompradorMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompradorMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHost = supportFragmentManager.findFragmentById(R.id.navHostComprador) as NavHostFragment
        val navController = navHost.navController
        binding.bottomNavComprador.setupWithNavController(navController)
    }
}
