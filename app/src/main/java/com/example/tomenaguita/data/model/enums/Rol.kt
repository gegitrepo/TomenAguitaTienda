package com.example.tomenaguita.data.model

enum class Rol(val valor: String) {
    COMPRADOR("comprador"),
    VENDEDOR("vendedor"),
    ADMINISTRADOR("administrador");

    companion object {
        fun fromString(valor: String) = entries.firstOrNull { it.valor == valor } ?: COMPRADOR
    }
}
