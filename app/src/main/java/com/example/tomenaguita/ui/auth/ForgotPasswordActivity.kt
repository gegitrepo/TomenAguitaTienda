package com.example.tomenaguita.ui.auth

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.tomenaguita.R
import com.example.tomenaguita.databinding.ActivityForgotPasswordBinding
import com.example.tomenaguita.utils.isValidEmail
import com.example.tomenaguita.utils.showSnackbar
import com.example.tomenaguita.viewmodel.AuthViewModel

// Pantalla de recuperación de contraseña de TomenAgüita.
// Permite al usuario ingresar su correo electrónico para recibir un enlace
// de restablecimiento enviado por Firebase Authentication.
class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding

    // ViewModel que gestiona el envío del correo de restablecimiento vía Firebase Auth
    private val viewModel: AuthViewModel by viewModels()

    // Infla el layout, configura el observador del resultado y los listeners de la UI.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()

        // Valida el correo y solicita el envío del enlace de restablecimiento al pulsar el botón
        binding.btnSend.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            if (!email.isValidEmail()) {
                binding.tilEmail.error = getString(R.string.error_email_invalid)
                return@setOnClickListener
            }
            binding.tilEmail.error = null
            binding.btnSend.isEnabled = false
            viewModel.sendPasswordReset(email)
        }

        // Cierra esta pantalla y vuelve al login al pulsar el enlace de regreso
        binding.tvBackLogin.setOnClickListener { finish() }
    }

    // Observa el resultado del envío del correo de restablecimiento.
    // En caso de éxito muestra un Snackbar con el correo destino y cierra la pantalla.
    // En caso de error muestra un Snackbar con el mensaje de la excepción.
    private fun setupObservers() {
        viewModel.resetResult.observe(this) { result ->
            binding.btnSend.isEnabled = true
            result.fold(
                onSuccess = {
                    binding.root.showSnackbar(getString(R.string.msg_reset_link_sent, binding.etEmail.text.toString().trim()))
                    finish()
                },
                onFailure = { binding.root.showSnackbar(it.message ?: getString(R.string.error_field_required)) }
            )
        }
    }
}
