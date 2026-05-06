package com.example.tomenaguita.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tomenaguita.R
import com.example.tomenaguita.databinding.ActivityRegisterBinding
import com.example.tomenaguita.utils.isValidColombian
import com.example.tomenaguita.utils.isValidEmail
import com.example.tomenaguita.utils.showSnackbar

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener { doRegister() }
        binding.tvLogin.setOnClickListener { finish() }
    }

    private fun doRegister() {
        val nombre = binding.etNombre.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val telefono = binding.etTelefono.text.toString().trim()
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()

        binding.tilNombre.error = null
        binding.tilEmail.error = null
        binding.tilTelefono.error = null
        binding.tilPassword.error = null
        binding.tilConfirmPassword.error = null

        if (nombre.length < 3) { binding.tilNombre.error = getString(R.string.error_name_short); return }
        if (!email.isValidEmail()) { binding.tilEmail.error = getString(R.string.error_email_invalid); return }
        if (!telefono.isValidColombian()) { binding.tilTelefono.error = getString(R.string.error_phone_invalid); return }
        if (password.length < 8) { binding.tilPassword.error = getString(R.string.error_password_short); return }
        if (password != confirmPassword) { binding.tilConfirmPassword.error = getString(R.string.error_passwords_no_match); return }
        if (!binding.cbTerminos.isChecked) { binding.root.showSnackbar(getString(R.string.error_terms_required)); return }

        // Demo: simular registro exitoso
        binding.root.showSnackbar(getString(R.string.msg_account_created))
        startActivity(Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        })
        finish()
    }
}
