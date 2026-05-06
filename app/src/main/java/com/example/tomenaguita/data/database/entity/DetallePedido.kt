package com.example.tomenaguita.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "detalle_pedidos")
data class DetallePedido(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val pedidoId: Long,
    val productoId: Long,
    val nombreProducto: String,
    val presentacion: String,
    val cantidad: Int,
    val precioUnitario: Double,
    val subtotal: Double,
    val vendedorId: Long
)
