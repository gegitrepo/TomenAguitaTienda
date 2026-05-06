package com.example.tomenaguita.ui.admin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tomenaguita.R
import com.example.tomenaguita.databinding.ActivityAdminMainBinding
import com.example.tomenaguita.ui.auth.LoginActivity
import com.example.tomenaguita.utils.SessionManager

class AdminMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminMainBinding
    private lateinit var appBarConfig: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val navHost = supportFragmentManager.findFragmentById(R.id.navHostAdmin) as NavHostFragment
        val navController = navHost.navController

        appBarConfig = AppBarConfiguration(
            setOf(R.id.dashboardFragment, R.id.listaUsuariosFragment, R.id.gestionProductosFragment, R.id.reporteVentasFragment),
            binding.drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfig)
        binding.navViewAdmin.setupWithNavController(navController)

        binding.navViewAdmin.setNavigationItemSelectedListener { item ->
            if (item.itemId == R.id.action_logout) {
                SessionManager(this).clearSession()
                startActivity(Intent(this, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                })
                true
            } else {
                try { navController.navigate(item.itemId) } catch (_: Exception) { }
                binding.drawerLayout.closeDrawers()
                true
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHost = supportFragmentManager.findFragmentById(R.id.navHostAdmin) as NavHostFragment
        return navHost.navController.navigateUp(appBarConfig) || super.onSupportNavigateUp()
    }
}
