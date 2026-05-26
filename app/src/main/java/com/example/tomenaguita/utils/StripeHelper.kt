package com.example.tomenaguita.utils

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

/*
 * Objeto de utilidad que encapsula la comunicación con la API de Stripe.
 * Proporciona una función suspendida para crear PaymentIntents desde el
 * cliente Android.
 *
 * ADVERTENCIA: llamar la API de Stripe con el secret key desde el cliente
 * solo es aceptable en entornos de prueba académicos. En producción, esta
 * lógica debe residir en un backend propio para proteger el secret key.
 */
object StripeHelper {

    // URL del endpoint de PaymentIntents construida a partir de la constante de base
    private val API_URL = "${Constants.STRIPE_BASE_URL}/v1/payment_intents"

    /**
     * Crea un PaymentIntent en Stripe y devuelve el client_secret necesario
     * para confirmar el pago desde el SDK de Stripe en el cliente.
     *
     * Consume:
     *   - amount: monto en la unidad principal de la moneda (ej. 15000 para 15 000 COP).
     *             Se multiplica por 100 internamente para enviar centavos a Stripe.
     *   - currency: código ISO de moneda en minúsculas (por defecto "cop").
     *
     * Devuelve: el client_secret del PaymentIntent creado.
     * Lanza Exception si Stripe responde con un código de error HTTP.
     *
     * HARDCODED: usa Constants.STRIPE_SECRET_KEY directamente en el cliente;
     *            solo valido en entorno de prueba academico.
     */
    suspend fun createPaymentIntent(amount: Int, currency: String = "cop"): String =
        withContext(Dispatchers.IO) {
            // Abre la conexion HTTP hacia la API de Stripe
            val connection = (URL(API_URL).openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
                setRequestProperty("Authorization", "Bearer ${Constants.STRIPE_SECRET_KEY}")
                setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
                doOutput = true
                connectTimeout = Constants.HTTP_TIMEOUT_MS
                readTimeout    = Constants.HTTP_TIMEOUT_MS
            }

            try {
                // Construye el cuerpo de la peticion convirtiendo el monto a centavos
                val body = "amount=${amount * 100}&currency=$currency&automatic_payment_methods[enabled]=true"
                Log.d("StripeHelper", "createPaymentIntent → amount=${amount * 100}, currency=$currency")
                connection.outputStream.use { it.write(body.toByteArray()) }

                // Lee la respuesta o el stream de error segun el codigo HTTP recibido
                val code = connection.responseCode
                val responseText = if (code in 200..299) {
                    connection.inputStream.bufferedReader().readText()
                } else {
                    connection.errorStream?.bufferedReader()?.readText() ?: "Error desconocido"
                }
                Log.d("StripeHelper", "Respuesta Stripe ($code): $responseText")

                // Si el codigo no es exitoso, extrae el mensaje de error del JSON y lanza excepcion
                if (code !in 200..299) {
                    val msg = runCatching {
                        JSONObject(responseText).getJSONObject("error").getString("message")
                    }.getOrDefault(responseText)
                    throw Exception("Stripe error ($code): $msg")
                }

                // Extrae y devuelve el client_secret del JSON de respuesta
                JSONObject(responseText).getString("client_secret")
            } finally {
                // Cierra siempre la conexion para liberar recursos
                connection.disconnect()
            }
        }
}
