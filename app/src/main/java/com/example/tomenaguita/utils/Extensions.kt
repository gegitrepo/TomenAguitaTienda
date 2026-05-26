package com.example.tomenaguita.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar
import java.text.NumberFormat
import java.util.Locale

/*
 * Funciones de extension reutilizables en toda la aplicacion.
 * Cubren formateo de precios en pesos colombianos, manejo de visibilidad
 * de vistas, mensajes Snackbar y validacion de datos de entrada del usuario.
 */

/**
 * Formatea un valor Double como precio en pesos colombianos (COP) usando
 * el locale "es_CO", sin decimales (NumberFormat con maximumFractionDigits = 0).
 *
 * Devuelve: cadena con el simbolo de moneda y el valor separado por puntos,
 *           por ejemplo "$ 15.000".
 */
fun Double.toCOP(): String {
    val format = NumberFormat.getCurrencyInstance(Locale("es", "CO"))
    format.maximumFractionDigits = 0
    return format.format(this)
}

/**
 * Muestra un Snackbar de duracion corta (LENGTH_SHORT) anclado a la View receptora.
 *
 * Consume: message — texto a mostrar en el Snackbar.
 */
fun View.showSnackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
}

// Hace la View visible (View.VISIBLE); equivalente a view.visibility = View.VISIBLE
fun View.visible() { visibility = View.VISIBLE }

// Oculta la View sin conservar su espacio en el layout (View.GONE)
fun View.gone() { visibility = View.GONE }

// Oculta la View pero conserva su espacio en el layout (View.INVISIBLE)
fun View.invisible() { visibility = View.INVISIBLE }

/**
 * Verifica si la cadena receptora tiene formato de correo electronico valido
 * usando el patron de Android (android.util.Patterns.EMAIL_ADDRESS).
 *
 * Devuelve: true si la cadena es un correo valido.
 */
fun String.isValidEmail(): Boolean =
    android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()

/**
 * Verifica si la cadena receptora es un numero de telefono movil colombiano valido.
 * El formato esperado es exactamente 10 digitos que comienzan con "3"
 * (operadores moviles colombianos: 300-329, 350-359, etc.).
 *
 * Devuelve: true si la cadena coincide con el patron de celular colombiano.
 */
fun String.isValidColombian(): Boolean =
    Regex("^3[0-9]{9}$").matches(this)
