package com.example.tomenaguita.ui.auth

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.tomenaguita.R
import com.example.tomenaguita.databinding.ActivityForgotPasswordBinding
import com.example.tomenaguita.utils.isValidEmail
import com.example.tomenaguita.utils.showSnackbar
import com.example.tomenaguita.viewmodel.AuthViewModel

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()

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

        binding.tvBackLogin.setOnClickListener { finish() }
    }

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
