package com.example.tomenaguita.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tomenaguita.databinding.ActivityForgotPasswordBinding
import com.example.tomenaguita.utils.isValidEmail
import com.example.tomenaguita.utils.showSnackbar

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSend.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            if (!email.isValidEmail()) {
                binding.tilEmail.error = "Correo inválido"
                return@setOnClickListener
            }
            binding.tilEmail.error = null
            binding.root.showSnackbar("Se envió el enlace a $email")
        }

        binding.tvBackLogin.setOnClickListener { finish() }
    }
}
