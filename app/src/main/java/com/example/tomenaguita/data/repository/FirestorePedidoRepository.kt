package com.example.tomenaguita.data.repository

import com.example.tomenaguita.data.database.entity.DetallePedido
import com.example.tomenaguita.data.database.entity.Pedido
import com.example.tomenaguita.data.model.EstadoPedido
import com.example.tomenaguita.utils.Constants
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/*
 * FirestorePedidoRepository
 *
 * Repositorio de pedidos en la nube. Gestiona la lectura y escritura de documentos
 * en la coleccion "pedidos" de Firestore (Google Cloud).
 *
 * Es la contraparte en la nube de PedidoRepository (Room). Mientras Room almacena
 * los pedidos localmente para respuesta inmediata en la UI, este repositorio garantiza
 * que los pedidos persistan en la nube y sean visibles para vendedores y administradores
 * en otros dispositivos en tiempo real.
 *
 * Estructura en Firestore:
 *   pedidos/{orderNumber}           -> documento raiz con la cabecera del pedido
 *   pedidos/{orderNumber}/detalles  -> subcoleccion con las lineas de detalle
 */
class FirestorePedidoRepository {

    // Referencia a la coleccion de pedidos en Firestore.
    private val col = FirebaseFirestore.getInstance().collection(Constants.FS_PEDIDOS)

    /*
     * Devuelve un Flow con los pedidos de un usuario especifico en tiempo real,
     * ordenados del mas reciente al mas antiguo.
     * El listener se cancela cuando el Flow deja de ser observado (awaitClose).
     *
     * Parametro:
     *   usuarioUid - UID de Firebase Auth del comprador propietario de los pedidos
     */
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

    /*
     * Devuelve un Flow con todos los pedidos en tiempo real, ordenados del mas
     * reciente al mas antiguo. Usado por administradores y vendedores para ver
     * el historial completo de ordenes.
     * El listener se cancela cuando el Flow deja de ser observado (awaitClose).
     */
    fun getAllPedidos(): Flow<List<Pedido>> = callbackFlow {
        val listener = col
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snap, err ->
                if (err != null) { close(err); return@addSnapshotListener }
                trySend(snap?.documents?.mapNotNull { it.toPedido() } ?: emptyList())
            }
        awaitClose { listener.remove() }
    }

    /*
     * Crea un pedido completo en Firestore:
     * 1. Guarda la cabecera del pedido como documento raiz usando el orderNumber como ID.
     * 2. Inserta cada DetallePedido en la subcoleccion "detalles" del documento raiz.
     *
     * Parametros:
     *   pedido     - entidad con los datos de la cabecera del pedido
     *   detalles   - lista de items que componen el pedido
     *   usuarioUid - UID de Firebase Auth del comprador, necesario para filtrar por usuario
     */
    suspend fun crearPedido(pedido: Pedido, detalles: List<DetallePedido>, usuarioUid: String) {
        // Usar el orderNumber como ID del documento para facilitar las busquedas posteriores.
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

        // Insertar cada detalle en la subcoleccion de detalles del pedido.
        detalles.forEach { detalle ->
            pedidoRef.collection(Constants.FS_DETALLES).add(mapOf(
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

    /*
     * Actualiza el estado de un pedido en Firestore.
     * Agrega automaticamente el campo "updatedAt" con la fecha y hora actuales.
     *
     * Parametros:
     *   orderNumber - numero de orden unico que identifica el pedido en Firestore
     *   estado      - nuevo estado del pedido (ej: "pendiente", "en_camino", "entregado")
     */
    suspend fun actualizarEstado(orderNumber: String, estado: String) {
        col.document(orderNumber)
            .update(mapOf("estado" to estado, "updatedAt" to Timestamp.now())).await()
    }

    /*
     * Lee los items de un pedido desde la subcoleccion "detalles" en Firestore.
     * Devuelve la lista de DetallePedido asociados al orderNumber indicado.
     * Util al sincronizar pedidos desde Firestore a Room cuando el dispositivo
     * no tiene los detalles almacenados localmente.
     *
     * Parametro:
     *   orderNumber - numero de orden unico que identifica el pedido en Firestore
     */
    suspend fun getDetallesByOrderNumber(orderNumber: String): List<DetallePedido> {
        val snap = col.document(orderNumber).collection(Constants.FS_DETALLES).get().await()
        return snap.documents.mapNotNull { doc ->
            DetallePedido(
                pedidoId   = 0L,
                productoId = doc.getLong("productoId") ?: return@mapNotNull null,
                vendedorId = doc.getLong("vendedorId") ?: 0L,
                nombreProducto = doc.getString("nombreProducto") ?: "",
                presentacion   = doc.getString("presentacion") ?: "",
                cantidad       = doc.getLong("cantidad")?.toInt() ?: 0,
                precioUnitario = doc.getDouble("precioUnitario") ?: 0.0,
                subtotal       = doc.getDouble("subtotal") ?: 0.0
            )
        }
    }

    /*
     * Funcion de extension privada que convierte un DocumentSnapshot de Firestore
     * en una entidad Pedido de Room. Devuelve null si falta el campo obligatorio
     * "orderNumber", evitando que datos incompletos lleguen a la base de datos local.
     */
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
            estado = getString("estado") ?: EstadoPedido.PENDIENTE.valor,
            metodoPago = getString("metodoPago") ?: "",
            transactionId = getString("transactionId")
        )
    }
}
