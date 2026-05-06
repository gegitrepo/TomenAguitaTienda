package com.example.tomenaguita.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String,
    val email: String,
    val password: String,
    val telefono: String,
    val rol: String = "comprador",
    val activo: Int = 1,
    val fotoUrl: String? = null,
    val direccion: String? = null,
    val latitud: Double? = null,
    val longitud: Double? = null,
    val biometricEnabled: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
