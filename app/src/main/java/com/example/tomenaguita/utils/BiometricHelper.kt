package com.example.tomenaguita.utils

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

class BiometricHelper(
    private val activity: FragmentActivity,
    private val onSuccess: () -> Unit,
    private val onError: (String) -> Unit
) {

    fun isAvailable(): Boolean {
        val manager = BiometricManager.from(activity)
        return manager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) ==
                BiometricManager.BIOMETRIC_SUCCESS
    }

    fun authenticate() {
        val executor = ContextCompat.getMainExecutor(activity)
        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                onSuccess()
            }
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                onError(errString.toString())
            }
            override fun onAuthenticationFailed() {
                onError("Autenticación fallida. Intenta de nuevo.")
            }
        }

        val prompt = BiometricPrompt(activity, executor, callback)
        val info = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Tomen Agüita")
            .setSubtitle("Ingresa con tu huella digital")
            .setNegativeButtonText("Cancelar")
            .build()

        prompt.authenticate(info)
    }
}
