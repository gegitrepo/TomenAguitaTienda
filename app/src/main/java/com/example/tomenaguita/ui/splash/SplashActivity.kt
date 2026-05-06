package com.example.tomenaguita.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tomenaguita.databinding.ActivitySplashBinding
import com.example.tomenaguita.ui.auth.LoginActivity
import com.example.tomenaguita.utils.SessionManager

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivLogo.animate().alpha(1f).setDuration(600).start()
        binding.tvAppName.animate().alpha(1f).setDuration(600).setStartDelay(200).start()
        binding.tvSlogan.animate().alpha(1f).setDuration(600).setStartDelay(400).withEndAction {
            navigateNext()
        }.start()
    }

    private fun navigateNext() {
        val session = SessionManager(this)
        val intent = if (session.isLoggedIn()) {
            getIntentForRol(session.getUserRol())
        } else {
            Intent(this, LoginActivity::class.java)
        }
        startActivity(intent)
        finish()
    }

    private fun getIntentForRol(rol: String?): Intent = when (rol) {
        "vendedor" -> Intent(this, com.example.tomenaguita.ui.vendedor.VendedorMainActivity::class.java)
        "administrador" -> Intent(this, com.example.tomenaguita.ui.admin.AdminMainActivity::class.java)
        else -> Intent(this, com.example.tomenaguita.ui.comprador.CompradorMainActivity::class.java)
    }
}
