package com.example.tomenaguita.ui.vendedor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.tomenaguita.R
import com.example.tomenaguita.databinding.ActivityVendedorMainBinding

class VendedorMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVendedorMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVendedorMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHost = supportFragmentManager.findFragmentById(R.id.navHostVendedor) as NavHostFragment
        val navController = navHost.navController

        binding.bottomNavVendedor.setupWithNavController(navController)

        // Mismo fix que CompradorMainActivity: evita multi-back-stack que deja
        // sub-fragments (crearEditarProducto) en la pila al cambiar de pestaña.
        binding.bottomNavVendedor.setOnItemSelectedListener { item ->
            val options = NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setPopUpTo(navController.graph.startDestinationId, inclusive = false)
                .build()
            try {
                navController.navigate(item.itemId, null, options)
                true
            } catch (_: Exception) {
                false
            }
        }
    }
}
