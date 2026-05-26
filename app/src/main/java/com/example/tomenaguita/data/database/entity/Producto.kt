package com.example.tomenaguita.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/*
 * Entidad Room que representa un producto de agua disponible en el catalogo.
 * Se persiste en la tabla "productos" y se sincroniza con la coleccion equivalente
 * en Firestore mediante el campo firestoreDocId.
 *
 * El campo eliminado actua como borrado logico para no perder el historial de pedidos
 * que referencian al producto. El campo disponible indica si aparece en el catalogo.
 * El vendedorId vincula el producto con el usuario vendedor que lo registro.
 */
@Entity(tableName = "productos")
data class Producto(
    // Identificador local autogenerado por Room
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    // Nombre comercial del producto (ej. "Botella personal")
    val nombre: String,
    // Descripcion detallada del producto
    val descripcion: String,
    // Presentacion del envase (ej. "300ml", "5L"); valores definidos en Constants.PRESENTACIONES
    val presentacion: String,
    // Precio de venta en pesos colombianos (COP)
    val precio: Double,
    // URL de la imagen del producto en Firebase Storage; null si no tiene imagen
    val imagenUrl: String? = null,
    // Indica si el producto esta disponible para la venta en el catalogo (1 = si, 0 = no)
    val disponible: Int = 1,
    // Cantidad de unidades en inventario
    val stock: Int,
    // ID del usuario vendedor dueno del producto (referencia a Usuario.id)
    val vendedorId: Long,
    // Borrado logico: 1 significa que el producto fue eliminado y no debe mostrarse
    val eliminado: Int = 0,
    // ID del documento correspondiente en Firestore; null hasta que se sincronice con la nube
    val firestoreDocId: String? = null,
    // Marca de tiempo de creacion del registro (milisegundos desde epoch)
    val createdAt: Long = System.currentTimeMillis(),
    // Marca de tiempo de la ultima actualizacion del registro (milisegundos desde epoch)
    val updatedAt: Long = System.currentTimeMillis()
)
