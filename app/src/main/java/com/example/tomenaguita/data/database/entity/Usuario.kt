package com.example.tomenaguita.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/*
 * Entidad Room que representa a un usuario registrado en la base de datos local.
 * Se persiste en la tabla "usuarios" y se sincroniza con la coleccion equivalente
 * en Firestore mediante el campo firestoreDocId.
 *
 * Roles posibles: "comprador", "vendedor", "administrador".
 * Los campos latitud/longitud almacenan la ubicacion de entrega del usuario.
 * El campo activo actua como borrado logico (1 = activo, 0 = inactivo).
 * El campo biometricEnabled indica si el usuario habilito inicio de sesion biometrico.
 */
@Entity(tableName = "usuarios")
data class Usuario(
    // Identificador local autogenerado por Room
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    // Nombre completo del usuario
    val nombre: String,
    // Correo electronico; se usa como identificador de inicio de sesion
    val email: String,
    // Contrasena del usuario (almacenada en la base local; Firebase Auth maneja la nube)
    val password: String,
    // Numero de telefono movil colombiano (10 digitos)
    val telefono: String,
    // Rol del usuario dentro de la aplicacion; por defecto "comprador"
    val rol: String = "comprador",
    // Indica si la cuenta esta activa (1) o desactivada logicamente (0)
    val activo: Int = 1,
    // URL de la foto de perfil almacenada en Firebase Storage; null si no tiene foto
    val fotoUrl: String? = null,
    // Direccion postal de entrega del usuario; null si no ha sido configurada
    val direccion: String? = null,
    // Latitud de la ubicacion de entrega; null si no ha sido configurada
    val latitud: Double? = null,
    // Longitud de la ubicacion de entrega; null si no ha sido configurada
    val longitud: Double? = null,
    // Indica si el usuario habilito autenticacion biometrica (0 = desactivada, 1 = activada)
    val biometricEnabled: Int = 0,
    // ID del documento correspondiente en Firestore; null hasta que se sincronice con la nube
    val firestoreDocId: String? = null,
    // Marca de tiempo de creacion del registro (milisegundos desde epoch)
    val createdAt: Long = System.currentTimeMillis(),
    // Marca de tiempo de la ultima actualizacion del registro (milisegundos desde epoch)
    val updatedAt: Long = System.currentTimeMillis()
)
