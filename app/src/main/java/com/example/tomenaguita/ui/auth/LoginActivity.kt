package com.example.tomenaguita.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.tomenaguita.R
import com.example.tomenaguita.databinding.ActivityLoginBinding
import com.example.tomenaguita.ui.admin.AdminMainActivity
import com.example.tomenaguita.ui.comprador.CompradorMainActivity
import com.example.tomenaguita.ui.vendedor.VendedorMainActivity
import com.example.tomenaguita.utils.BiometricHelper
import com.example.tomenaguita.utils.SessionManager
import com.example.tomenaguita.utils.isValidEmail
import com.example.tomenaguita.utils.showSnackbar
import com.example.tomenaguita.viewmodel.AuthViewModel

// Pantalla de inicio de sesión de la app TomenAgüita.
// Permite al usuario autenticarse con correo y contraseña, o mediante biometría
// si el dispositivo lo soporta y el usuario lo tiene habilitado.
// Tras un login exitoso redirige a la pantalla principal según el rol del usuario.
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var session: SessionManager

    // ViewModel que gestiona las operaciones de autenticación con Firebase Auth
    private val viewModel: AuthViewModel by viewModels()

    // Infla el layout, inicializa el SessionManager y configura observadores,
    // listeners de UI y autenticación biométrica.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        session = SessionManager(this)

        setupObservers()
        setupListeners()
        setupBiometric()
    }

    // Observa el resultado del login emitido por el ViewModel.
    // En caso de éxito navega al destino correspondiente al rol del usuario.
    // En caso de error muestra un Snackbar con el mensaje de la excepción.
    private fun setupObservers() {
        viewModel.loginResult.observe(this) { result ->
            binding.btnLogin.isEnabled = true
            result.fold(
                onSuccess = { navigateTo(session.getUserRol()) },
                onFailure = { binding.root.showSnackbar(it.message ?: getString(R.string.msg_login_failed)) }
            )
        }
    }

    // Asigna los listeners de los botones y enlaces de la pantalla:
    // botón de login, enlace de registro y enlace de contraseña olvidada.
    private fun setupListeners() {
        binding.btnLogin.setOnClickListener { doLogin() }
        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        binding.tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }

    // Configura el botón de autenticación biométrica.
    // Solo se habilita si el hardware está disponible y el usuario
    // tiene la biometría activada en la sesión guardada.
    private fun setupBiometric() {
        val helper = BiometricHelper(this,
            onSuccess = { navigateTo(session.getUserRol()) },
            onError = { msg -> binding.root.showSnackbar(msg) }
        )
        if (helper.isAvailable() && session.isBiometricEnabled()) {
            binding.btnBiometric.isEnabled = true
            binding.btnBiometric.setOnClickListener { helper.authenticate() }
        } else {
            binding.btnBiometric.isEnabled = false
        }
    }

    // Valida los campos de correo y contraseña y, si son correctos,
    // deshabilita el botón para evitar envíos duplicados y llama al ViewModel.
    // Consume: texto de los campos etEmail y etPassword.
    // No devuelve valor; el resultado llega por el LiveData loginResult.
    private fun doLogin() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()

        binding.tilEmail.error = null
        binding.tilPassword.error = null

        if (email.isEmpty()) { binding.tilEmail.error = getString(R.string.error_field_required); return }
        if (!email.isValidEmail()) { binding.tilEmail.error = getString(R.string.error_email_invalid); return }
        if (password.isEmpty()) { binding.tilPassword.error = getString(R.string.error_field_required); return }
        if (password.length < 8) { binding.tilPassword.error = getString(R.string.error_password_short); return }

        binding.btnLogin.isEnabled = false
        viewModel.login(email, password)
    }

    // Construye el Intent de la actividad principal según el rol y navega hacia ella.
    // Limpia el back stack para que el usuario no pueda volver al login con el botón atrás.
    // Consume: el rol del usuario (String nullable).
    private fun navigateTo(rol: String?) {
        val intent = when (rol) {
            "vendedor" -> Intent(this, VendedorMainActivity::class.java)
            "administrador" -> Intent(this, AdminMainActivity::class.java)
            else -> Intent(this, CompradorMainActivity::class.java)
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
