package com.example.tomenaguita.data.repository

import com.example.tomenaguita.data.database.entity.Producto
import com.example.tomenaguita.utils.Constants
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirestoreProductoRepository {

    private val col = FirebaseFirestore.getInstance().collection("productos")

    fun getAllDisponibles(): Flow<List<Producto>> = callbackFlow {
        val listener = col
            .whereEqualTo("disponible", true)
            .whereEqualTo("eliminado", false)
            .addSnapshotListener { snap, err ->
                if (err != null) { close(err); return@addSnapshotListener }
                trySend(snap?.documents?.mapNotNull { it.toProducto() } ?: emptyList())
            }
        awaitClose { listener.remove() }
    }

    fun getByVendedor(vendedorUid: String): Flow<List<Producto>> = callbackFlow {
        val listener = col
            .whereEqualTo("vendedorId", vendedorUid)
            .whereEqualTo("eliminado", false)
            .addSnapshotListener { snap, err ->
                if (err != null) { close(err); return@addSnapshotListener }
                trySend(snap?.documents?.mapNotNull { it.toProducto() } ?: emptyList())
            }
        awaitClose { listener.remove() }
    }

    suspend fun insert(producto: Producto, vendedorUid: String): String {
        val ref = col.add(mapOf(
            "nombre" to producto.nombre,
            "descripcion" to producto.descripcion,
            "presentacion" to producto.presentacion,
            "precio" to producto.precio,
            "imagenUrl" to (producto.imagenUrl ?: ""),
            "disponible" to (producto.disponible == 1),
            "stock" to producto.stock,
            "vendedorId" to vendedorUid,
            "eliminado" to false,
            "createdAt" to Timestamp.now(),
            "updatedAt" to Timestamp.now()
        )).await()
        return ref.id
    }

    suspend fun update(docId: String, campos: Map<String, Any>) {
        col.document(docId).update(campos + mapOf("updatedAt" to Timestamp.now())).await()
    }

    suspend fun softDelete(docId: String) = update(docId, mapOf("eliminado" to true))

    suspend fun seedProductosIfEmpty() {
        val snap = col.limit(1).get().await()
        if (!snap.isEmpty) return
        Constants.PRODUCTOS_DEMO.forEach { (nombre, presentacion, precio) ->
            col.add(mapOf(
                "nombre" to nombre,
                "descripcion" to "Agua embotellada $presentacion",
                "presentacion" to presentacion,
                "precio" to precio,
                "imagenUrl" to "",
                "disponible" to true,
                "stock" to 100,
                "vendedorId" to "demo",
                "eliminado" to false,
                "createdAt" to Timestamp.now(),
                "updatedAt" to Timestamp.now()
            )).await()
        }
    }

    private fun DocumentSnapshot.toProducto(): Producto? {
        val nombre = getString("nombre") ?: return null
        return Producto(
            id = 0L,
            nombre = nombre,
            descripcion = getString("descripcion") ?: "",
            presentacion = getString("presentacion") ?: "",
            precio = getDouble("precio") ?: 0.0,
            imagenUrl = getString("imagenUrl")?.takeIf { it.isNotEmpty() },
            disponible = if (getBoolean("disponible") == true) 1 else 0,
            stock = getLong("stock")?.toInt() ?: 0,
            vendedorId = 0L,
            eliminado = if (getBoolean("eliminado") == true) 1 else 0,
            firestoreDocId = id
        )
    }
}
