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

/*
 * FirestoreProductoRepository
 *
 * Repositorio de productos en la nube. Gestiona la lectura y escritura de
 * documentos en la coleccion "productos" de Firestore (Google Cloud).
 *
 * Es la contraparte en la nube de ProductoRepository (Room). Mientras Room
 * almacena el catalogo localmente para respuesta inmediata en la UI, este
 * repositorio garantiza que los productos persistan en la nube y se compartan
 * entre todos los dispositivos en tiempo real.
 */
class FirestoreProductoRepository {

    // Referencia a la coleccion de productos en Firestore.
    private val col = FirebaseFirestore.getInstance().collection(Constants.FS_PRODUCTOS)

    /*
     * Devuelve un Flow con los productos disponibles y no eliminados en tiempo real.
     * Aplica filtros directamente en la consulta de Firestore para traer solo
     * los documentos con disponible=true y eliminado=false.
     * El listener se cancela cuando el Flow deja de ser observado (awaitClose).
     */
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

    /*
     * Devuelve un Flow con los productos de un vendedor especifico en tiempo real.
     * Filtra por el UID de Firebase del vendedor y excluye los productos eliminados.
     * El listener se cancela cuando el Flow deja de ser observado (awaitClose).
     *
     * Parametro:
     *   vendedorUid - UID de Firebase Auth del vendedor propietario de los productos
     */
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

    /*
     * Crea un nuevo documento de producto en Firestore.
     * Devuelve el ID del documento generado automaticamente por Firestore,
     * que se guarda en Room como firestoreDocId para futuras sincronizaciones.
     *
     * Parametros:
     *   producto    - entidad con los datos del producto a publicar
     *   vendedorUid - UID de Firebase Auth del vendedor que publica el producto
     */
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

    /*
     * Actualiza campos especificos del documento de un producto en Firestore.
     * Agrega automaticamente el campo "updatedAt" con la fecha y hora actuales.
     *
     * Parametros:
     *   docId  - ID del documento en Firestore
     *   campos - mapa de campos a actualizar con sus nuevos valores
     */
    suspend fun update(docId: String, campos: Map<String, Any>) {
        col.document(docId).update(campos + mapOf("updatedAt" to Timestamp.now())).await()
    }

    // Marca un producto como eliminado en Firestore (borrado logico) sin eliminar el documento.
    // Recibe el ID del documento en Firestore.
    suspend fun softDelete(docId: String) = update(docId, mapOf("eliminado" to true))

    /*
     * Puebla la coleccion "productos" en Firestore con datos de demostracion
     * solo si la coleccion esta vacia. Evita duplicar productos al iniciar la app
     * en un entorno limpio. Los datos de demo estan definidos en Constants.PRODUCTOS_DEMO.
     */
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

    /*
     * Funcion de extension privada que convierte un DocumentSnapshot de Firestore
     * en una entidad Producto de Room. Devuelve null si falta el campo obligatorio
     * "nombre", evitando que datos incompletos lleguen a la base de datos local.
     */
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
