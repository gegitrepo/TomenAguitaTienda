package com.example.tomenaguita.data.repository

import com.example.tomenaguita.data.database.entity.CarritoItem
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirestoreCarritoRepository {

    private val firestore = FirebaseFirestore.getInstance()

    private fun itemsRef(userUid: String) =
        firestore.collection("carritos").document(userUid).collection("items")

    fun getCarrito(userUid: String): Flow<List<CarritoItem>> = callbackFlow {
        val listener = itemsRef(userUid).addSnapshotListener { snap, err ->
            if (err != null) { close(err); return@addSnapshotListener }
            val items = snap?.documents?.mapNotNull { doc ->
                CarritoItem(
                    id = doc.getLong("roomId") ?: 0L,
                    usuarioId = doc.getLong("usuarioId") ?: 0L,
                    productoId = doc.getLong("productoId") ?: 0L,
                    cantidad = doc.getLong("cantidad")?.toInt() ?: 1,
                    precioAlMomento = doc.getDouble("precioAlMomento") ?: 0.0
                )
            } ?: emptyList()
            trySend(items)
        }
        awaitClose { listener.remove() }
    }

    fun getCount(userUid: String): Flow<Int> = callbackFlow {
        val listener = itemsRef(userUid).addSnapshotListener { snap, _ ->
            trySend(snap?.size() ?: 0)
        }
        awaitClose { listener.remove() }
    }

    suspend fun insert(userUid: String, item: CarritoItem) {
        itemsRef(userUid).document(item.productoId.toString()).set(mapOf(
            "roomId" to item.id,
            "usuarioId" to item.usuarioId,
            "productoId" to item.productoId,
            "cantidad" to item.cantidad,
            "precioAlMomento" to item.precioAlMomento,
            "updatedAt" to Timestamp.now()
        )).await()
    }

    suspend fun update(userUid: String, productoId: Long, cantidad: Int) {
        itemsRef(userUid).document(productoId.toString())
            .update(mapOf("cantidad" to cantidad, "updatedAt" to Timestamp.now())).await()
    }

    suspend fun delete(userUid: String, productoId: Long) {
        itemsRef(userUid).document(productoId.toString()).delete().await()
    }

    suspend fun vaciar(userUid: String) {
        val docs = itemsRef(userUid).get().await()
        docs.forEach { it.reference.delete().await() }
    }
}
