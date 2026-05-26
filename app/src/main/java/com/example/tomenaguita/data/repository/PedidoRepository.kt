package com.example.tomenaguita.data.repository

import com.example.tomenaguita.data.database.dao.PedidoDao
import com.example.tomenaguita.data.database.entity.DetallePedido
import com.example.tomenaguita.data.database.entity.Pedido
import kotlinx.coroutines.flow.Flow

/*
 * PedidoRepository
 *
 * Repositorio local de pedidos que actua como fuente de verdad para la UI.
 * Encapsula todas las operaciones sobre la base de datos Room (SQLite local)
 * a traves del PedidoDao. Gestiona tanto la cabecera del pedido (Pedido) como
 * sus lineas de detalle (DetallePedido), que se almacenan en tablas separadas.
 *
 * Patron: Room es la fuente de verdad local; Firestore (FirestorePedidoRepository)
 * es la contraparte en la nube que persiste los pedidos y los hace visibles
 * a vendedores y administradores en otros dispositivos.
 */
class PedidoRepository(private val dao: PedidoDao) {

    // Devuelve un Flow con los pedidos realizados por un usuario especifico,
    // identificado por su ID local de Room.
    fun getPedidosByUsuario(usuarioId: Long): Flow<List<Pedido>> = dao.getPedidosByUsuario(usuarioId)

    // Devuelve un Flow con todos los pedidos almacenados en Room.
    // Usado principalmente por administradores para ver el historial completo.
    fun getAllPedidos(): Flow<List<Pedido>> = dao.getAllPedidos()

    // Devuelve un Flow con los pedidos asociados a un vendedor especifico,
    // identificado por su ID local de Room.
    fun getPedidosByVendedor(vendedorId: Long): Flow<List<Pedido>> = dao.getPedidosByVendedor(vendedorId)

    // Busca y devuelve un pedido por su ID local de Room. Devuelve null si no existe.
    suspend fun getById(id: Long): Pedido? = dao.getPedidoById(id)

    // Devuelve la lista de items (DetallePedido) pertenecientes a un pedido especifico.
    // Recibe el ID local del pedido en Room.
    suspend fun getDetalles(pedidoId: Long): List<DetallePedido> = dao.getDetallesByPedido(pedidoId)

    /*
     * Crea un pedido completo de forma atomica en Room:
     * 1. Inserta la cabecera del pedido y obtiene el ID generado.
     * 2. Inserta cada DetallePedido asignandole el pedidoId correcto.
     * Devuelve el ID local del pedido recien creado.
     */
    suspend fun crearPedido(pedido: Pedido, detalles: List<DetallePedido>): Long {
        val id = dao.insertPedido(pedido)
        detalles.forEach { dao.insertDetalle(it.copy(pedidoId = id)) }
        return id
    }

    // Actualiza el estado de un pedido en Room (por ejemplo: "pendiente", "en camino", "entregado").
    // Recibe el ID local del pedido y el nuevo valor del estado.
    suspend fun actualizarEstado(id: Long, estado: String) = dao.actualizarEstado(id, estado)

    // Busca un pedido por su numero de orden (orderNumber), que es un identificador legible
    // y unico generado en el momento de crear el pedido. Devuelve null si no existe.
    suspend fun getByOrderNumber(orderNumber: String): Pedido? = dao.getByOrderNumber(orderNumber)

    // Inserta una lista de items de detalle asociandolos a un pedido ya existente en Room.
    // Util para sincronizar los detalles que llegan desde Firestore cuando el pedido ya fue creado.
    suspend fun insertarDetalles(pedidoId: Long, detalles: List<DetallePedido>) {
        detalles.forEach { dao.insertDetalle(it.copy(pedidoId = pedidoId)) }
    }
}
