package com.example.tomenaguita.ui.comprador

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.tomenaguita.R
import com.example.tomenaguita.databinding.ActivityCompradorMainBinding

// Actividad principal del flujo comprador en TomenAgüita.
// Aloja el NavHostFragment con el grafo de navegación del comprador y
// gestiona la barra de navegación inferior (BottomNavigationView) que
// da acceso a Home, Catálogo, Carrito, Pedidos y Perfil.
class CompradorMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCompradorMainBinding

    // Infla el layout, enlaza el NavController con la BottomNavigationView y
    // configura el listener de selección de ítems para navegar con opciones
    // que evitan duplicar destinos en el back stack.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompradorMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtiene el NavController desde el NavHostFragment declarado en el layout
        val navHost = supportFragmentManager.findFragmentById(R.id.navHostComprador) as NavHostFragment
        val navController = navHost.navController

        // Conecta la barra inferior con el NavController para resaltar el ítem activo automáticamente
        binding.bottomNavComprador.setupWithNavController(navController)

        // Listener personalizado para controlar las opciones de navegación:
        // evita múltiples instancias del mismo destino y limpia el back stack hasta el inicio
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
