package com.example.tomenaguita.utils

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

/*
 * Clase auxiliar que encapsula la logica de autenticacion biometrica (huella digital)
 * usando la API de AndroidX Biometric. Recibe callbacks para notificar el resultado
 * al componente que la instancie (Activity o Fragment).
 *
 * Consume al construirse:
 *   - activity: FragmentActivity desde la que se muestra el dialogo biometrico.
 *   - onSuccess: lambda invocada cuando la autenticacion biometrica es exitosa.
 *   - onError: lambda invocada con un mensaje de error cuando falla o es cancelada.
 */
class BiometricHelper(
    private val activity: FragmentActivity,
    private val onSuccess: () -> Unit,
    private val onError: (String) -> Unit
) {

    /**
     * Verifica si el dispositivo soporta autenticacion biometrica fuerte (BIOMETRIC_STRONG)
     * y si el usuario tiene al menos una huella registrada.
     *
     * Devuelve: true si la autenticacion biometrica esta disponible y lista para usarse.
     */
    fun isAvailable(): Boolean {
        val manager = BiometricManager.from(activity)
        return manager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) ==
                BiometricManager.BIOMETRIC_SUCCESS
    }

    /**
     * Lanza el dialogo de autenticacion biometrica del sistema.
     * Los resultados se entregan a traves de los callbacks onSuccess y onError
     * proporcionados al construir la instancia.
     *
     * Muestra el titulo "Tomen Aguita" y el subtitulo "Ingresa con tu huella digital".
     */
    fun authenticate() {
        // El executor asegura que los callbacks se ejecuten en el hilo principal
        val executor = ContextCompat.getMainExecutor(activity)

        // Define los tres casos posibles de resultado del dialogo biometrico
        val callback = object : BiometricPrompt.AuthenticationCallback() {
            // Autenticacion completada con exito
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                onSuccess()
            }
            // Error del sistema o cancelacion por parte del usuario
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                onError(errString.toString())
            }
            // El intento de huella no coincidio; el sistema puede permitir reintentos
            override fun onAuthenticationFailed() {
                onError("Autenticación fallida. Intenta de nuevo.")
            }
        }

        val prompt = BiometricPrompt(activity, executor, callback)

        // Configura los textos del dialogo biometrico que ve el usuario
        val info = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Tomen Agüita")
            .setSubtitle("Ingresa con tu huella digital")
            .setNegativeButtonText("Cancelar")
            .build()

        prompt.authenticate(info)
    }
}
