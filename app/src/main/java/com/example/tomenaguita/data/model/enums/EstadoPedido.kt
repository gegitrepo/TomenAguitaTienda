package com.example.tomenaguita.data.model

/*
 * Enumeracion que define los estados posibles del ciclo de vida de un pedido
 * en la aplicacion TomenAgüita.
 *
 * Cada constante tiene un valor de cadena (valor) que coincide con el texto
 * almacenado en Room y en Firestore, facilitando la serializacion.
 *
 * Flujo normal de estados:
 *   PENDIENTE -> PAGADO -> ENVIADO -> ENTREGADO
 * El estado CANCELADO es terminal y puede ocurrir desde cualquier estado previo.
 */
enum class EstadoPedido(val valor: String) {
    // El pedido fue creado pero aun no se ha procesado el pago
    PENDIENTE("pendiente"),
    // El pago fue confirmado por Stripe; el vendedor debe preparar el envio
    PAGADO("pagado"),
    // El pedido fue despachado y esta en camino al comprador
    ENVIADO("enviado"),
    // El comprador recibio el pedido; estado final exitoso
    ENTREGADO("entregado"),
    // El pedido fue cancelado; estado final sin entrega
    CANCELADO("cancelado");

    /**
     * Devuelve el siguiente estado logico en el flujo del pedido.
     *
     * Devuelve: el EstadoPedido siguiente, o null si el estado actual es terminal
     *           (ENTREGADO o CANCELADO no tienen estado siguiente).
     */
    fun siguiente(): EstadoPedido? = when (this) {
        PENDIENTE  -> PAGADO
        PAGADO     -> ENVIADO
        ENVIADO    -> ENTREGADO
        ENTREGADO, CANCELADO -> null
    }

    companion object {
        /**
         * Convierte una cadena de texto en el EstadoPedido correspondiente.
         *
         * Consume: valor — cadena con el estado (ej. "pagado", "enviado").
         * Devuelve: el EstadoPedido que coincide con el valor, o PENDIENTE si no hay coincidencia.
         */
        fun fromString(valor: String) = entries.firstOrNull { it.valor == valor } ?: PENDIENTE
    }
}
