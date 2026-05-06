package com.example.tomenaguita.data.repository

import com.example.tomenaguita.data.database.dao.CarritoDao
import com.example.tomenaguita.data.database.entity.CarritoItem
import kotlinx.coroutines.flow.Flow

class CarritoRepository(private val dao: CarritoDao) {

    fun getCarrito(usuarioId: Long): Flow<List<CarritoItem>> = dao.getCarritoByUsuario(usuarioId)

    fun getCount(usuarioId: Long): Flow<Int> = dao.getCarritoCount(usuarioId)

    suspend fun insert(item: CarritoItem): Long = dao.insert(item)

    suspend fun update(item: CarritoItem) = dao.update(item)

    suspend fun delete(id: Long) = dao.delete(id)

    suspend fun vaciar(usuarioId: Long) = dao.vaciarCarrito(usuarioId)
}
