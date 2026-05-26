package com.example.tomenaguita.ui.adapter

import com.example.tomenaguita.data.database.entity.Producto

// Modelo de presentación (UI model) para un producto en el RecyclerView del catálogo.
// Aplana y simplifica la entidad Producto de Room exponiendo solo los campos
// necesarios para la vista, sin exponer detalles de la capa de datos.
data class ProductoItem(
    val id: Long,
    val nombre: String,
    // Texto descriptivo del envase o tamaño (ej: "Botella 600ml", "Bolsa 500ml")
    val presentacion: String,
    val precio: Double,
    val stock: Int,
    // Indica si el producto está disponible para ser agregado al carrito
    val disponible: Boolean,
    // URL de la imagen del producto almacenada en Firebase Storage; puede ser nula
    val imagenUrl: String? = null
) {
    companion object {
        // Convierte una entidad Producto de Room en un ProductoItem para la UI.
        // Parámetros: producto es la entidad de la base de datos local.
        // Devuelve un ProductoItem listo para ser mostrado en el adaptador.
        fun from(producto: Producto) = ProductoItem(
            id = producto.id,
            nombre = producto.nombre,
            presentacion = producto.presentacion,
            precio = producto.precio,
            stock = producto.stock,
            // Room almacena disponible como Int (1/0); se convierte a Boolean para la UI
            disponible = producto.disponible == 1,
            imagenUrl = producto.imagenUrl
        )
    }
}
