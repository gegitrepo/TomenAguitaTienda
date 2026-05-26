package com.example.tomenaguita.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tomenaguita.data.model.EstadoPedido

/*
 * Entidad Room que representa la cabecera de un pedido realizado por un comprador.
 * Se persiste en la tabla "pedidos" y se sincroniza con Firestore.
 *
 * Un pedido agrupa uno o varios DetallePedido (productos comprados). El estado
 * sigue el flujo definido en EstadoPedido: pendiente -> pagado -> enviado -> entregado.
 * El campo transactionId vincula el pedido con el PaymentIntent de Stripe.
 * Las coordenadas latitud/longitud almacenan el punto de entrega seleccionado.
 */
@Entity(tableName = "pedidos")
data class Pedido(
    // Identificador local autogenerado por Room
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    // Numero de orden legible generado por la app (ej. "ORD-20240521-0001")
    val orderNumber: String,
    // ID del usuario comprador que realizo el pedido (referencia a Usuario.id)
    val usuarioId: Long,
    // Suma de los subtotales de todos los productos del pedido (en COP)
    val totalProductos: Double,
    // Costo de envio del pedido; actualmente siempre 0.0 (envio gratuito)
    val costoEnvio: Double = 0.0,
    // Total final del pedido: totalProductos + costoEnvio (en COP)
    val totalPedido: Double,
    // Direccion postal de entrega en texto legible
    val direccionEntrega: String,
    // Latitud del punto de entrega seleccionado en el mapa; null si no se uso mapa
    val latitud: Double? = null,
    // Longitud del punto de entrega seleccionado en el mapa; null si no se uso mapa
    val longitud: Double? = null,
    // Estado actual del pedido; valores definidos en EstadoPedido
    val estado: String = EstadoPedido.PENDIENTE.valor,
    // Metodo de pago usado (ej. "stripe", "efectivo")
    val metodoPago: String,
    // ID de la transaccion de Stripe (client_secret o PaymentIntent ID); null si no aplica
    val transactionId: String? = null,
    // Marca de tiempo de creacion del pedido (milisegundos desde epoch)
    val createdAt: Long = System.currentTimeMillis(),
    // Marca de tiempo de la ultima actualizacion del pedido (milisegundos desde epoch)
    val updatedAt: Long = System.currentTimeMillis()
)
