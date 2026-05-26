package com.example.tomenaguita.utils

/*
 * Objeto singleton que centraliza todas las constantes de la aplicación TomenAgüita.
 * Agrupa claves de SharedPreferences, listas de dominio, configuración de mapas,
 * credenciales de Stripe (solo para entorno de prueba), nombres de colecciones
 * Firestore, y datos de demostración de productos.
 */
object Constants {

    // Claves para guardar y leer la sesión del usuario en SharedPreferences cifradas
    const val PREF_USER_ID             = "user_id"
    const val PREF_USER_ROL            = "user_rol"
    const val PREF_USER_EMAIL          = "user_email"
    const val PREF_USER_NOMBRE         = "user_nombre"
    const val PREF_BIOMETRIC_ENABLED   = "biometric_enabled"

    // Lista de presentaciones de envase disponibles para los productos de agua
    val PRESENTACIONES = listOf("300ml", "500ml", "1L", "5L", "20L")

    // Roles de usuario reconocidos por la aplicación
    val ROLES = listOf("comprador", "vendedor", "administrador")

    // Costo de envío fijo aplicado a cada pedido (actualmente gratuito)
    const val COSTO_ENVIO = 0.0

    // ── Credenciales Stripe (test/sandbox) ──────────────────────────────────
    // ADVERTENCIA: el secret key nunca debe estar en el cliente en producción.
    // Para producción, mover createPaymentIntent a un backend propio.
    // HARDCODED: entorno académico de prueba; en producción va en el servidor.
    const val STRIPE_PUBLISHABLE_KEY = "pk_test_51TZfJQGoQuxetubIX17frE0y9xlxEDMoM4VdyfpVIEgLs0sNInPiIQyNo1jNdAESWDcbHlaYVZLwiZnaV4RvqFQF00ky7tG8zV"
    const val STRIPE_SECRET_KEY      = "sk_test_51TZfJQGoQuxetubIO3o9gZ67ClNlucMoajET5Ogiks2bEueXnquYtrkxDCfNjxJiMdJgCzJBja0V2Ps7x7CW0rUo00ctPZIFxW"

    // ── Configuración de mapas ───────────────────────────────────────────────
    const val MAP_CITY_ZOOM     = 12f      // Nivel de zoom inicial para vista de ciudad
    const val MAP_LOCATION_ZOOM = 15f      // Nivel de zoom al centrar en la ubicación del usuario
    // HARDCODED: coordenadas de Bogotá como punto de referencia geográfico del mercado objetivo
    const val MAP_DEFAULT_LAT   = 4.7110   // Latitud de Bogotá
    const val MAP_DEFAULT_LNG   = -74.0721 // Longitud de Bogotá

    // ── Timeouts de red ──────────────────────────────────────────────────────
    // Tiempo máximo de espera en milisegundos para llamadas a APIs externas
    const val HTTP_TIMEOUT_MS = 15_000

    // ── Límites de UI ────────────────────────────────────────────────────────
    // Número máximo de pedidos que se muestran en el reporte de ventas
    const val MAX_REPORT_ITEMS = 50
    // Cantidad máxima permitida de un mismo producto por carrito
    const val MAX_PRODUCT_QTY  = 99

    // ── Colecciones Firestore ────────────────────────────────────────────────
    // Nombres de las colecciones en la base de datos en la nube (Firestore)
    const val FS_PEDIDOS   = "pedidos"
    const val FS_PRODUCTOS = "productos"
    const val FS_USUARIOS  = "usuarios"
    const val FS_CARRITOS  = "carritos"
    const val FS_DETALLES  = "detalles"

    // ── URLs de APIs ─────────────────────────────────────────────────────────
    // URL base de la API de Stripe; se combina con rutas específicas en StripeHelper
    const val STRIPE_BASE_URL = "https://api.stripe.com"

    /*
     * Catálogo de productos de demostración usado para pre-poblar la base de datos
     * en la primera instalación de la app. Cada Triple contiene: nombre, descripción
     * de presentación y precio en pesos colombianos (COP).
     */
    val PRODUCTOS_DEMO = listOf(
        Triple("Botella personal",  "300 ml",      1500.0),
        Triple("Botella mediana",   "500 ml",      2500.0),
        Triple("Botella familiar",  "1 litro",     4000.0),
        Triple("Botellón",          "5 litros",   12000.0),
        Triple("Pack personal",     "24×300 ml",  30000.0),
        Triple("Pack mediano",      "12×500 ml",  25000.0),
        Triple("Pack familiar",     "6×1 litro",  20000.0),
        Triple("Garrafón",          "20 litros",  18000.0)
    )
}
