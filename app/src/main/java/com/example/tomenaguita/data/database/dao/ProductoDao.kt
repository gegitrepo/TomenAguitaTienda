package com.example.tomenaguita.data.database.dao

import androidx.room.*
import com.example.tomenaguita.data.database.entity.Producto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {
    @Query("SELECT * FROM productos WHERE eliminado = 0 AND disponible = 1 ORDER BY nombre ASC")
    fun getAllProductosDisponibles(): Flow<List<Producto>>

    @Query("SELECT * FROM productos WHERE vendedorId = :vendedorId AND eliminado = 0 ORDER BY nombre ASC")
    fun getProductosByVendedor(vendedorId: Long): Flow<List<Producto>>

    @Query("SELECT * FROM productos WHERE id = :id AND eliminado = 0")
    suspend fun getProductoById(id: Long): Producto?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(producto: Producto): Long

    @Update
    suspend fun update(producto: Producto)

    @Query("UPDATE productos SET eliminado = 1, updatedAt = :timestamp WHERE id = :id")
    suspend fun softDelete(id: Long, timestamp: Long = System.currentTimeMillis())

    @Query("SELECT * FROM productos WHERE firestoreDocId = :docId LIMIT 1")
    suspend fun getByFirestoreDocId(docId: String): Producto?
}
