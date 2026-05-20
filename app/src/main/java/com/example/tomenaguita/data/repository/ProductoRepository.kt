package com.example.tomenaguita.data.repository

import com.example.tomenaguita.data.database.dao.ProductoDao
import com.example.tomenaguita.data.database.entity.Producto
import kotlinx.coroutines.flow.Flow

class ProductoRepository(private val dao: ProductoDao) {

    fun getAllDisponibles(): Flow<List<Producto>> = dao.getAllProductosDisponibles()

    fun getByVendedor(vendedorId: Long): Flow<List<Producto>> = dao.getProductosByVendedor(vendedorId)

    suspend fun getById(id: Long): Producto? = dao.getProductoById(id)

    suspend fun insert(producto: Producto): Long = dao.insert(producto)

    suspend fun update(producto: Producto) = dao.update(producto)

    suspend fun delete(id: Long) = dao.softDelete(id)

    suspend fun getByFirestoreDocId(docId: String): Producto? = dao.getByFirestoreDocId(docId)
}
