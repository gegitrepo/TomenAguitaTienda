package com.example.tomenaguita.data.repository

import com.example.tomenaguita.data.database.dao.PedidoDao
import com.example.tomenaguita.data.database.entity.DetallePedido
import com.example.tomenaguita.data.database.entity.Pedido
import kotlinx.coroutines.flow.Flow

class PedidoRepository(private val dao: PedidoDao) {

    fun getPedidosByUsuario(usuarioId: Long): Flow<List<Pedido>> = dao.getPedidosByUsuario(usuarioId)

    fun getAllPedidos(): Flow<List<Pedido>> = dao.getAllPedidos()

    suspend fun getById(id: Long): Pedido? = dao.getPedidoById(id)

    suspend fun getDetalles(pedidoId: Long): List<DetallePedido> = dao.getDetallesByPedido(pedidoId)

    suspend fun crearPedido(pedido: Pedido, detalles: List<DetallePedido>): Long {
        val id = dao.insertPedido(pedido)
        detalles.forEach { dao.insertDetalle(it.copy(pedidoId = id)) }
        return id
    }

    suspend fun actualizarEstado(id: Long, estado: String) = dao.actualizarEstado(id, estado)
}
