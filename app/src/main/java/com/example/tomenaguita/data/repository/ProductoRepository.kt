package com.example.tomenaguita.data.repository

import com.example.tomenaguita.data.database.dao.ProductoDao
import com.example.tomenaguita.data.database.entity.Producto
import kotlinx.coroutines.flow.Flow

/*
 * ProductoRepository
 *
 * Repositorio local de productos que actua como fuente de verdad para la UI.
 * Encapsula todas las operaciones sobre la base de datos Room (SQLite local)
 * a traves del ProductoDao. Los ViewModels consumen datos desde Room via Flow,
 * lo que permite actualizaciones reactivas en la pantalla sin necesidad de
 * llamadas a la red en cada consulta.
 *
 * Patron: Room es la fuente de verdad local; Firestore (FirestoreProductoRepository)
 * es la contraparte en la nube y sincroniza el catalogo de productos entre dispositivos.
 */
class ProductoRepository(private val dao: ProductoDao) {

    // Devuelve un Flow con todos los productos disponibles (no eliminados) almacenados en Room.
    // La UI se actualiza automaticamente cuando cambia el catalogo local.
    fun getAllDisponibles(): Flow<List<Producto>> = dao.getAllProductosDisponibles()

    // Devuelve un Flow con los productos pertenecientes a un vendedor especifico,
    // identificado por su ID local de Room.
    fun getByVendedor(vendedorId: Long): Flow<List<Producto>> = dao.getProductosByVendedor(vendedorId)

    // Busca y devuelve un producto por su ID local de Room. Devuelve null si no existe.
    suspend fun getById(id: Long): Producto? = dao.getProductoById(id)

    // Inserta un nuevo producto en Room y devuelve el ID local generado.
    suspend fun insert(producto: Producto): Long = dao.insert(producto)

    // Actualiza los datos de un producto existente en Room.
    suspend fun update(producto: Producto) = dao.update(producto)

    // Marca un producto como eliminado en Room sin borrarlo fisicamente (borrado logico).
    // Recibe el ID local del producto a eliminar.
    suspend fun delete(id: Long) = dao.softDelete(id)

    // Busca un producto por su ID de documento en Firestore (firestoreDocId).
    // Util para sincronizar registros de Firestore con su contraparte local en Room.
    // Devuelve null si no se encuentra ninguna coincidencia.
    suspend fun getByFirestoreDocId(docId: String): Producto? = dao.getByFirestoreDocId(docId)
}
