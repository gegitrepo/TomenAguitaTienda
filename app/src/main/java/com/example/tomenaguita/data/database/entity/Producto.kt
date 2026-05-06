package com.example.tomenaguita.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class Producto(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String,
    val descripcion: String,
    val presentacion: String,
    val precio: Double,
    val imagenUrl: String? = null,
    val disponible: Int = 1,
    val stock: Int,
    val vendedorId: Long,
    val eliminado: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
