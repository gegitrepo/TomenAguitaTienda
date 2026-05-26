package com.example.tomenaguita.ui.vendedor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.tomenaguita.R
import com.example.tomenaguita.databinding.ActivityVendedorMainBinding

// Actividad principal del flujo vendedor.
// Contiene el NavHostFragment del vendedor y gestiona la barra de navegacion inferior (BottomNav).
// Las pestanas disponibles son: Mis Productos, Pedidos Recibidos y Perfil.
class VendedorMainActivity : AppCompatActivity() {

    // Referencia al binding generado desde activity_vendedor_main.xml
    private lateinit var binding: ActivityVendedorMainBinding

    // Inicializa la actividad, vincula el NavController con la BottomNav
    // y configura el listener de seleccion de items para evitar duplicados en el back stack.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVendedorMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtiene el NavController desde el NavHostFragment declarado en el layout
        val navHost = supportFragmentManager.findFragmentById(R.id.navHostVendedor) as NavHostFragment
        val navController = navHost.navController

        // Vincula la BottomNav con el NavController para que la seleccion de pestanas navegue automaticamente
        binding.bottomNavVendedor.setupWithNavController(navController)

        // Mismo fix que CompradorMainActivity: evita multi-back-stack que deja
        // sub-fragments (crearEditarProducto) en la pila al cambiar de pestaña.
        binding.bottomNavVendedor.setOnItemSelectedListener { item ->
            // Construye opciones de navegacion que limpian el back stack hasta el destino raiz
            // para evitar que sub-fragmentos queden apilados al cambiar de pestana
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
