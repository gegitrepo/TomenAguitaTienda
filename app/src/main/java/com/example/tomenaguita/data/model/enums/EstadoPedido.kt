package com.example.tomenaguita.data.model

enum class EstadoPedido(val valor: String) {
    PENDIENTE("pendiente"),
    PAGADO("pagado"),
    ENVIADO("enviado"),
    ENTREGADO("entregado"),
    CANCELADO("cancelado");

    fun siguiente(): EstadoPedido? = when (this) {
        PENDIENTE -> PAGADO
        PAGADO -> ENVIADO
        ENVIADO -> ENTREGADO
        ENTREGADO, CANCELADO -> null
    }

    companion object {
        fun fromString(valor: String) = entries.firstOrNull { it.valor == valor } ?: PENDIENTE
    }
}
