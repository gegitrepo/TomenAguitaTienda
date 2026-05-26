package com.example.tomenaguita.ui.admin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tomenaguita.R
import com.example.tomenaguita.databinding.ActivityAdminMainBinding
import com.example.tomenaguita.ui.auth.LoginActivity
import com.example.tomenaguita.utils.SessionManager
import com.google.firebase.auth.FirebaseAuth

// Actividad principal del modulo administrador.
// A diferencia de los modulos de vendedor y comprador, usa un Navigation Drawer lateral
// en lugar de una barra de navegacion inferior (BottomNavigationView).
// Contiene el NavHostFragment del admin y gestiona la navegacion entre las secciones:
// Dashboard, Lista de Usuarios, Gestion de Productos y Reporte de Ventas.
// Tambien maneja el cierre de sesion desde el item de menu del drawer.
class AdminMainActivity : AppCompatActivity() {

    // Binding para acceder a las vistas del layout activity_admin_main.xml
    private lateinit var binding: ActivityAdminMainBinding

    // Configuracion de la barra de acciones que define los destinos de nivel superior
    // (aquellos que muestran el icono de hamburguesa en lugar del boton de retroceso)
    private lateinit var appBarConfig: AppBarConfiguration

    // Controlador de navegacion del NavHostFragment del admin
    private lateinit var navController: androidx.navigation.NavController

    // Inicializa la interfaz, configura el toolbar, el NavController y el Navigation Drawer.
    // Vincula el NavigationView con el NavController para que los items del drawer naveguen automaticamente.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Asigna el toolbar personalizado como ActionBar de la actividad
        setSupportActionBar(binding.toolbar)

        // Obtiene el NavController desde el NavHostFragment definido en el layout
        val navHost = supportFragmentManager.findFragmentById(R.id.navHostAdmin) as NavHostFragment
        navController = navHost.navController

        // Define los destinos de nivel superior; en estos destinos se muestra el icono del drawer
        // y el DrawerLayout se puede abrir con el gesto de deslizar desde el borde izquierdo
        appBarConfig = AppBarConfiguration(
            setOf(R.id.dashboardFragment, R.id.listaUsuariosFragment, R.id.gestionProductosFragment, R.id.reporteVentasFragment),
            binding.drawerLayout
        )

        // Conecta el ActionBar con el NavController para mostrar el titulo del destino actual
        setupActionBarWithNavController(navController, appBarConfig)

        // Conecta el NavigationView con el NavController para resaltar el item activo automaticamente
        binding.navViewAdmin.setupWithNavController(navController)

        // Listener personalizado del NavigationView para manejar el cierre de sesion
        // y la navegacion con opciones que evitan apilar destinos duplicados en el back stack
        binding.navViewAdmin.setNavigationItemSelectedListener { item ->
            if (item.itemId == R.id.action_logout) {
                // Cierra la sesion de Firebase y la sesion local, luego redirige al login
                FirebaseAuth.getInstance().signOut()
                SessionManager(this).clearSession()
                startActivity(Intent(this, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                })
                true
            } else {
                // Navega al destino seleccionado evitando duplicados en el back stack
                // popUpTo evita que el drawer apile destinos duplicados en el back stack
                val options = NavOptions.Builder()
                    .setLaunchSingleTop(true)
                    .setPopUpTo(navController.graph.startDestinationId, inclusive = false)
                    .build()
                try { navController.navigate(item.itemId, null, options) } catch (_: Exception) { }
                binding.drawerLayout.closeDrawers()
                true
            }
        }
    }

    // Delega la navegacion hacia arriba al NavController; si no puede manejarla, la gestiona la actividad base.
    // Esto permite que el boton de retroceso del ActionBar funcione correctamente con el Navigation Drawer.
    override fun onSupportNavigateUp(): Boolean =
        navController.navigateUp(appBarConfig) || super.onSupportNavigateUp()
}
