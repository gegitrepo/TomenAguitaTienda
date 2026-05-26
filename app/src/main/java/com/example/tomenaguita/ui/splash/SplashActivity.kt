package com.example.tomenaguita.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tomenaguita.databinding.ActivitySplashBinding
import com.example.tomenaguita.ui.auth.LoginActivity
import com.example.tomenaguita.utils.SessionManager

// Pantalla de bienvenida (splash) que se muestra al iniciar la app.
// Anima el logo, el nombre y el slogan en secuencia y luego redirige al destino
// correcto según el estado de sesión y el rol del usuario.
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    // Infla el layout, lanza las animaciones y, al terminar la última, decide
    // a qué pantalla navegar.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Anima el logo, el nombre y el slogan en cadena con retrasos escalonados
        binding.ivLogo.animate().alpha(1f).setDuration(600).start()
        binding.tvAppName.animate().alpha(1f).setDuration(600).setStartDelay(200).start()
        binding.tvSlogan.animate().alpha(1f).setDuration(600).setStartDelay(400).withEndAction {
            navigateNext()
        }.start()
    }

    // Consulta el SessionManager para saber si el usuario ya tiene sesión activa.
    // Si la tiene, redirige a la pantalla principal según su rol; si no, va al login.
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

    // Devuelve el Intent de la actividad principal correspondiente al rol recibido.
    // Rol "vendedor" -> VendedorMainActivity
    // Rol "administrador" -> AdminMainActivity
    // Cualquier otro rol (incluido comprador) -> CompradorMainActivity
    private fun getIntentForRol(rol: String?): Intent = when (rol) {
        "vendedor" -> Intent(this, com.example.tomenaguita.ui.vendedor.VendedorMainActivity::class.java)
        "administrador" -> Intent(this, com.example.tomenaguita.ui.admin.AdminMainActivity::class.java)
        else -> Intent(this, com.example.tomenaguita.ui.comprador.CompradorMainActivity::class.java)
    }
}
