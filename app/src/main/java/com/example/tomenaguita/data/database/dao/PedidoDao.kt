package com.example.tomenaguita.data.database.dao

import androidx.room.*
import com.example.tomenaguita.data.database.entity.DetallePedido
import com.example.tomenaguita.data.database.entity.Pedido
import kotlinx.coroutines.flow.Flow

@Dao
interface PedidoDao {
    @Query("SELECT * FROM pedidos WHERE usuarioId = :usuarioId ORDER BY createdAt DESC")
    fun getPedidosByUsuario(usuarioId: Long): Flow<List<Pedido>>

    @Query("SELECT * FROM pedidos ORDER BY createdAt DESC")
    fun getAllPedidos(): Flow<List<Pedido>>

    @Query("SELECT * FROM pedidos WHERE id = :id")
    suspend fun getPedidoById(id: Long): Pedido?

    @Query("SELECT * FROM detalle_pedidos WHERE pedidoId = :pedidoId")
    suspend fun getDetallesByPedido(pedidoId: Long): List<DetallePedido>

    @Insert
    suspend fun insertPedido(pedido: Pedido): Long

    @Insert
    suspend fun insertDetalle(detalle: DetallePedido)

    @Query("UPDATE pedidos SET estado = :estado, updatedAt = :timestamp WHERE id = :id")
    suspend fun actualizarEstado(id: Long, estado: String, timestamp: Long = System.currentTimeMillis())
}
