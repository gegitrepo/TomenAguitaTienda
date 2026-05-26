package com.example.tomenaguita.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/*
 * Entidad Room que representa el detalle de un producto dentro de un pedido.
 * Se persiste en la tabla "detalle_pedidos". Cada fila corresponde a una linea
 * del pedido: un producto especifico con su cantidad y precio al momento de comprar.
 *
 * Los campos nombreProducto, presentacion y precioUnitario se copian del producto
 * en el momento de la compra para preservar el historial aunque el producto cambie.
 * El campo vendedorId permite filtrar pedidos por vendedor en PedidoDao.
 */
@Entity(tableName = "detalle_pedidos")
data class DetallePedido(
    // Identificador local autogenerado por Room
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    // ID del pedido al que pertenece este detalle (referencia a Pedido.id)
    val pedidoId: Long,
    // ID del producto comprado (referencia a Producto.id)
    val productoId: Long,
    // Nombre del producto en el momento de la compra (snapshot para historial)
    val nombreProducto: String,
    // Presentacion del envase en el momento de la compra (snapshot para historial)
    val presentacion: String,
    // Cantidad de unidades compradas de este producto
    val cantidad: Int,
    // Precio unitario del producto en el momento de la compra (en COP)
    val precioUnitario: Double,
    // Subtotal calculado: precioUnitario * cantidad (en COP)
    val subtotal: Double,
    // ID del vendedor dueno del producto; permite agrupar ventas por vendedor
    val vendedorId: Long
)
