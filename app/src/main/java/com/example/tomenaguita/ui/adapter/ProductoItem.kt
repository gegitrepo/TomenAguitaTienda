package com.example.tomenaguita.ui.adapter

import com.example.tomenaguita.data.database.entity.Producto

data class ProductoItem(
    val id: Long,
    val nombre: String,
    val presentacion: String,
    val precio: Double,
    val stock: Int,
    val disponible: Boolean,
    val imagenUrl: String? = null
) {
    companion object {
        fun from(producto: Producto) = ProductoItem(
            id = producto.id,
            nombre = producto.nombre,
            presentacion = producto.presentacion,
            precio = producto.precio,
            stock = producto.stock,
            disponible = producto.disponible == 1,
            imagenUrl = producto.imagenUrl
        )
    }
}
