package com.example.tomenaguita.utils

object Constants {
    const val PREF_USER_ID = "user_id"
    const val PREF_USER_ROL = "user_rol"
    const val PREF_USER_EMAIL = "user_email"
    const val PREF_USER_NOMBRE = "user_nombre"
    const val PREF_BIOMETRIC_ENABLED = "biometric_enabled"

    val PRESENTACIONES = listOf("300ml", "500ml", "1L", "5L", "20L")
    val ROLES = listOf("comprador", "vendedor", "administrador")

    const val COSTO_ENVIO = 0.0

    // Credenciales de PRUEBA (sandbox) de Mercado Pago — nunca usar en producción
    const val MP_SANDBOX_ACCESS_TOKEN = "APP_USR-7127794535848181-051821-942b17d70ca66e01cb8c3db5a71d068d-3410484757"
    const val MP_SANDBOX_PUBLIC_KEY   = "APP_USR-55c98dba-38ca-4003-af98-a187f40b6182"

    // Email del Buyer Test User de MP — obligatorio en sandbox para que el pago no sea rechazado
    // Seller: TESTUSER8879678697068368906 (User ID: 3410484757)
    // Buyer:  TESTUSER1685025191177064552 (User ID: 3410484759)
    const val MP_BUYER_TEST_EMAIL = "test_user_3410484759@testuser.com"

    // URLs internas que el WebViewClient intercepta para detectar el resultado del pago
    const val MP_URL_SUCCESS = "tomenaguita.app/pago/success"
    const val MP_URL_FAILURE = "tomenaguita.app/pago/failure"
    const val MP_URL_PENDING = "tomenaguita.app/pago/pending"

    val PRODUCTOS_DEMO = listOf(
        Triple("Botella personal", "300 ml", 1500.0),
        Triple("Botella mediana", "500 ml", 2500.0),
        Triple("Botella familiar", "1 litro", 4000.0),
        Triple("Botellón", "5 litros", 12000.0),
        Triple("Pack personal", "24×300 ml", 30000.0),
        Triple("Pack mediano", "12×500 ml", 25000.0),
        Triple("Pack familiar", "6×1 litro", 20000.0),
        Triple("Garrafón", "20 litros", 18000.0)
    )
}
