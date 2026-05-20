package com.example.tomenaguita.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

object MercadoPagoHelper {

    private const val API_URL = "https://api.mercadopago.com/checkout/preferences"
    private val client = OkHttpClient()

    /**
     * Crea una preferencia de pago en Mercado Pago Sandbox.
     *
     * Sin `payer.email` el checkout obliga al comprador a iniciar sesión,
     * lo cual es necesario en sandbox Colombia para que las tarjetas de prueba funcionen.
     *
     * Buyer test user: TESTUSER1685025191177064552 / 9FzRrFjciM
     * Tarjetas: Mastercard 5254 1336 7440 3564 · Visa 4013 5406 8274 6260
     * Titular: APRO (aprobado) · CVV: 123 · Venc: 11/30 · DNI: 123456789
     */
    suspend fun crearPreferencia(
        accessToken: String,
        titulo: String,
        cantidad: Int,
        precioUnitario: Double,
        emailComprador: String,
        referenciaExterna: String
    ): String = withContext(Dispatchers.IO) {

        val json = JSONObject().apply {
            put("items", JSONArray().apply {
                put(JSONObject().apply {
                    put("title", titulo)
                    put("quantity", cantidad)
                    put("unit_price", precioUnitario)
                    put("currency_id", "COP")
                })
            })
            put("external_reference", referenciaExterna)
            put("back_urls", JSONObject().apply {
                put("success", "https://${Constants.MP_URL_SUCCESS}")
                put("failure", "https://${Constants.MP_URL_FAILURE}")
                put("pending", "https://${Constants.MP_URL_PENDING}")
            })
            // auto_return solo aplica para pagos aprobados
            put("auto_return", "approved")
        }

        val body = json.toString().toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url(API_URL)
            .addHeader("Authorization", "Bearer $accessToken")
            .post(body)
            .build()

        val response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            throw Exception("Error Mercado Pago (${response.code}): ${response.body?.string()}")
        }

        val responseJson = JSONObject(response.body?.string() ?: throw Exception("Respuesta vacía"))
        responseJson.getString("sandbox_init_point")
    }
}
