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

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var session: SessionManager
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        session = SessionManager(this)

        setupObservers()
        setupListeners()
        setupBiometric()
    }

    private fun setupObservers() {
        viewModel.loginResult.observe(this) { result ->
            binding.btnLogin.isEnabled = true
            result.fold(
                onSuccess = { navigateTo(session.getUserRol()) },
                onFailure = { binding.root.showSnackbar(it.message ?: getString(R.string.msg_login_failed)) }
            )
        }
    }

    private fun setupListeners() {
        binding.btnLogin.setOnClickListener { doLogin() }
        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        binding.tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }

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
