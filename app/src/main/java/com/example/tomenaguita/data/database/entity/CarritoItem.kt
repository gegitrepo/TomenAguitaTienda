package com.example.tomenaguita.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "carrito")
data class CarritoItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val usuarioId: Long,
    val productoId: Long,
    val cantidad: Int,
    val precioAlMomento: Double,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
