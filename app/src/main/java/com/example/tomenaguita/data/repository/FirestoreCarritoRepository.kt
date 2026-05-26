package com.example.tomenaguita.data.repository

import com.example.tomenaguita.data.database.entity.CarritoItem
import com.example.tomenaguita.utils.Constants
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/*
 * FirestoreCarritoRepository
 *
 * Repositorio del carrito de compras en la nube. Gestiona la lectura y escritura
 * de items en Firestore (Google Cloud) con la estructura:
 *   carritos/{userUid}/items/{productoId}
 *
 * Es la contraparte en la nube de CarritoRepository (Room). Mientras Room almacena
 * el carrito localmente para respuesta inmediata en la UI, este repositorio
 * garantiza que el carrito persista en la nube y se sincronice entre dispositivos.
 *
 * El carrito de cada usuario se almacena como un documento raiz en la coleccion
 * "carritos" y sus items en una subcoleccion "items", usando el productoId como
 * ID del documento de cada item para evitar duplicados.
 */
class FirestoreCarritoRepository {

    // Instancia de Firestore para acceder a la base de datos en la nube.
    private val firestore = FirebaseFirestore.getInstance()

    // Devuelve la referencia a la subcoleccion "items" del carrito de un usuario especifico.
    // La ruta en Firestore es: carritos/{userUid}/items
    private fun itemsRef(userUid: String) =
        firestore.collection(Constants.FS_CARRITOS).document(userUid).collection("items")

    /*
     * Devuelve un Flow con la lista de items del carrito de un usuario en tiempo real.
     * Convierte cada documento de Firestore en un CarritoItem de Room para que
     * la UI pueda procesarlos con el mismo modelo de datos local.
     * El listener se cancela cuando el Flow deja de ser observado (awaitClose).
     *
     * Parametro:
     *   userUid - UID de Firebase Auth del usuario propietario del carrito
     */
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

    /*
     * Devuelve un Flow con el numero de items distintos en el carrito del usuario.
     * Util para mostrar el badge (contador) en el icono del carrito.
     * El listener se cancela cuando el Flow deja de ser observado (awaitClose).
     *
     * Parametro:
     *   userUid - UID de Firebase Auth del usuario propietario del carrito
     */
    fun getCount(userUid: String): Flow<Int> = callbackFlow {
        val listener = itemsRef(userUid).addSnapshotListener { snap, _ ->
            trySend(snap?.size() ?: 0)
        }
        awaitClose { listener.remove() }
    }

    /*
     * Inserta o sobreescribe un item en el carrito del usuario en Firestore.
     * Usa el productoId como ID del documento para garantizar que no haya
     * duplicados del mismo producto en el carrito.
     *
     * Parametros:
     *   userUid - UID de Firebase Auth del usuario propietario del carrito
     *   item    - datos del item a guardar, incluyendo cantidad y precio al momento
     */
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

    /*
     * Actualiza la cantidad de un item existente en el carrito del usuario en Firestore.
     *
     * Parametros:
     *   userUid    - UID de Firebase Auth del usuario propietario del carrito
     *   productoId - ID del producto cuya cantidad se desea actualizar
     *   cantidad   - nueva cantidad del producto en el carrito
     */
    suspend fun update(userUid: String, productoId: Long, cantidad: Int) {
        itemsRef(userUid).document(productoId.toString())
            .update(mapOf("cantidad" to cantidad, "updatedAt" to Timestamp.now())).await()
    }

    /*
     * Elimina un item del carrito del usuario en Firestore.
     *
     * Parametros:
     *   userUid    - UID de Firebase Auth del usuario propietario del carrito
     *   productoId - ID del producto a eliminar del carrito
     */
    suspend fun delete(userUid: String, productoId: Long) {
        itemsRef(userUid).document(productoId.toString()).delete().await()
    }

    /*
     * Elimina todos los items del carrito del usuario en Firestore.
     * Se usa al confirmar un pedido o al cerrar sesion.
     * Recorre todos los documentos de la subcoleccion y los elimina uno a uno.
     *
     * Parametro:
     *   userUid - UID de Firebase Auth del usuario cuyo carrito se va a vaciar
     */
    suspend fun vaciar(userUid: String) {
        val docs = itemsRef(userUid).get().await()
        docs.forEach { it.reference.delete().await() }
    }
}
