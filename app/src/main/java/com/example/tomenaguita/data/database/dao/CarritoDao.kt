package com.example.tomenaguita.data.database.dao

import androidx.room.*
import com.example.tomenaguita.data.database.entity.CarritoItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CarritoDao {
    @Query("SELECT * FROM carrito WHERE usuarioId = :usuarioId ORDER BY createdAt ASC")
    fun getCarritoByUsuario(usuarioId: Long): Flow<List<CarritoItem>>

    @Query("SELECT COUNT(*) FROM carrito WHERE usuarioId = :usuarioId")
    fun getCarritoCount(usuarioId: Long): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CarritoItem): Long

    @Update
    suspend fun update(item: CarritoItem)

    @Query("DELETE FROM carrito WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM carrito WHERE usuarioId = :usuarioId")
    suspend fun vaciarCarrito(usuarioId: Long)
}
