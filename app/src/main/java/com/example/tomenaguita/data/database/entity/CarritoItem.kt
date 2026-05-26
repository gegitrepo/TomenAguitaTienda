package com.example.tomenaguita.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/*
 * Entidad Room que representa un item dentro del carrito de compras de un usuario.
 * Se persiste en la tabla "carrito". Cada fila vincula un usuario con un producto
 * y almacena la cantidad seleccionada y el precio vigente en el momento de agregar
 * el item (precioAlMomento), para proteger contra cambios de precio antes de confirmar.
 *
 * El carrito es estrictamente local (Room); no se sincroniza con Firestore.
 */
@Entity(tableName = "carrito")
data class CarritoItem(
    // Identificador local autogenerado por Room
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    // ID del usuario propietario del carrito (referencia a Usuario.id)
    val usuarioId: Long,
    // ID del producto agregado al carrito (referencia a Producto.id)
    val productoId: Long,
    // Cantidad de unidades del producto; maximo definido en Constants.MAX_PRODUCT_QTY
    val cantidad: Int,
    // Precio unitario del producto en el momento de agregarlo al carrito (en COP)
    val precioAlMomento: Double,
    // Marca de tiempo de creacion del item en el carrito (milisegundos desde epoch)
    val createdAt: Long = System.currentTimeMillis(),
    // Marca de tiempo de la ultima modificacion del item (milisegundos desde epoch)
    val updatedAt: Long = System.currentTimeMillis()
)
