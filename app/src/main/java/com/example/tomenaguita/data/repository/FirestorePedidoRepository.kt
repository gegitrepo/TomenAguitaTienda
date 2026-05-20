package com.example.tomenaguita.data.repository

import com.example.tomenaguita.data.database.entity.DetallePedido
import com.example.tomenaguita.data.database.entity.Pedido
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirestorePedidoRepository {

    private val col = FirebaseFirestore.getInstance().collection("pedidos")

    fun getPedidosByUsuario(usuarioUid: String): Flow<List<Pedido>> = callbackFlow {
        val listener = col
            .whereEqualTo("usuarioUid", usuarioUid)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snap, err ->
                if (err != null) { close(err); return@addSnapshotListener }
                trySend(snap?.documents?.mapNotNull { it.toPedido() } ?: emptyList())
            }
        awaitClose { listener.remove() }
    }

    fun getAllPedidos(): Flow<List<Pedido>> = callbackFlow {
        val listener = col
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snap, err ->
                if (err != null) { close(err); return@addSnapshotListener }
                trySend(snap?.documents?.mapNotNull { it.toPedido() } ?: emptyList())
            }
        awaitClose { listener.remove() }
    }

    suspend fun crearPedido(pedido: Pedido, detalles: List<DetallePedido>, usuarioUid: String) {
        val pedidoRef = col.document(pedido.orderNumber)
        pedidoRef.set(mapOf(
            "orderNumber" to pedido.orderNumber,
            "usuarioId" to pedido.usuarioId,
            "usuarioUid" to usuarioUid,
            "totalProductos" to pedido.totalProductos,
            "costoEnvio" to pedido.costoEnvio,
            "totalPedido" to pedido.totalPedido,
            "direccionEntrega" to pedido.direccionEntrega,
            "latitud" to pedido.latitud,
            "longitud" to pedido.longitud,
            "estado" to pedido.estado,
            "metodoPago" to pedido.metodoPago,
            "transactionId" to pedido.transactionId,
            "createdAt" to Timestamp.now(),
            "updatedAt" to Timestamp.now()
        )).await()
        detalles.forEach { detalle ->
            pedidoRef.collection("detalles").add(mapOf(
                "productoId" to detalle.productoId,
                "vendedorId" to detalle.vendedorId,
                "nombreProducto" to detalle.nombreProducto,
                "presentacion" to detalle.presentacion,
                "cantidad" to detalle.cantidad,
                "precioUnitario" to detalle.precioUnitario,
                "subtotal" to detalle.subtotal
            )).await()
        }
    }

    suspend fun actualizarEstado(orderNumber: String, estado: String) {
        col.document(orderNumber)
            .update(mapOf("estado" to estado, "updatedAt" to Timestamp.now())).await()
    }

    private fun DocumentSnapshot.toPedido(): Pedido? {
        val orderNumber = getString("orderNumber") ?: return null
        return Pedido(
            id = 0L,
            orderNumber = orderNumber,
            usuarioId = getLong("usuarioId") ?: 0L,
            totalProductos = getDouble("totalProductos") ?: 0.0,
            costoEnvio = getDouble("costoEnvio") ?: 0.0,
            totalPedido = getDouble("totalPedido") ?: 0.0,
            direccionEntrega = getString("direccionEntrega") ?: "",
            latitud = getDouble("latitud"),
            longitud = getDouble("longitud"),
            estado = getString("estado") ?: "pendiente",
            metodoPago = getString("metodoPago") ?: "",
            transactionId = getString("transactionId")
        )
    }
}
