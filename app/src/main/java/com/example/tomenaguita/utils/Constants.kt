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
