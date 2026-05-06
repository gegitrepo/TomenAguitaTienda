package com.example.tomenaguita.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pedidos")
data class Pedido(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val orderNumber: String,
    val usuarioId: Long,
    val totalProductos: Double,
    val costoEnvio: Double = 0.0,
    val totalPedido: Double,
    val direccionEntrega: String,
    val latitud: Double? = null,
    val longitud: Double? = null,
    val estado: String = "pendiente",
    val metodoPago: String,
    val transactionId: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
