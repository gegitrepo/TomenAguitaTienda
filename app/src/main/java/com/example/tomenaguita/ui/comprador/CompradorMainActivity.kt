package com.example.tomenaguita.ui.comprador

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
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

        // setupWithNavController registra el listener de cambio de destino para resaltar
        // el ítem correcto del BottomNav — lo conservamos para ese propósito.
        binding.bottomNavComprador.setupWithNavController(navController)

        // Sobreescribimos SOLO el listener de clics para evitar el comportamiento de
        // multi-back-stack (saveState/restoreState) que causaba que productoDetalleFragment
        // permaneciera en la pila al volver a la pestaña de catálogo u inicio.
        binding.bottomNavComprador.setOnItemSelectedListener { item ->
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
