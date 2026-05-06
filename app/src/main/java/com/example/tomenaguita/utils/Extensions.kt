package com.example.tomenaguita.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar
import java.text.NumberFormat
import java.util.Locale

fun Double.toCOP(): String {
    val format = NumberFormat.getCurrencyInstance(Locale("es", "CO"))
    format.maximumFractionDigits = 0
    return format.format(this)
}

fun View.showSnackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
}

fun View.visible() { visibility = View.VISIBLE }
fun View.gone() { visibility = View.GONE }
fun View.invisible() { visibility = View.INVISIBLE }

fun String.isValidEmail(): Boolean =
    android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isValidColombian(): Boolean =
    Regex("^3[0-9]{9}$").matches(this)
