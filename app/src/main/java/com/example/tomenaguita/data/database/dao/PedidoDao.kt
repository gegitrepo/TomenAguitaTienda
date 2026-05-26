package com.example.tomenaguita.data.database.dao

import androidx.room.*
import com.example.tomenaguita.data.database.entity.DetallePedido
import com.example.tomenaguita.data.database.entity.Pedido
import kotlinx.coroutines.flow.Flow

/*
 * DAO (Data Access Object) para las tablas "pedidos" y "detalle_pedidos".
 * Define todas las operaciones de acceso a datos para las entidades Pedido y DetallePedido:
 * consultas reactivas con Flow para el historial del comprador, vista del administrador
 * y vista del vendedor, busquedas puntuales, insercion y actualizacion de estado.
 */
@Dao
interface PedidoDao {

    /**
     * Devuelve todos los pedidos de un comprador especifico, del mas reciente al mas antiguo.
     * Emite una nueva lista cada vez que hay cambios en la tabla (Flow reactivo).
     * Usado en el historial de compras del comprador.
     *
     * Consume: usuarioId — ID del usuario comprador.
     */
    @Query("SELECT * FROM pedidos WHERE usuarioId = :usuarioId ORDER BY createdAt DESC")
    fun getPedidosByUsuario(usuarioId: Long): Flow<List<Pedido>>

    /**
     * Devuelve todos los pedidos de todos los usuarios, del mas reciente al mas antiguo.
     * Emite una nueva lista cada vez que hay cambios en la tabla (Flow reactivo).
     * Usado en el panel de administrador.
     */
    @Query("SELECT * FROM pedidos ORDER BY createdAt DESC")
    fun getAllPedidos(): Flow<List<Pedido>>

    /**
     * Devuelve los pedidos que contienen al menos un producto del vendedor indicado.
     * Usa un JOIN con detalle_pedidos para filtrar por vendedorId y DISTINCT para
     * evitar duplicados cuando el pedido tiene varios productos del mismo vendedor.
     * Emite una nueva lista cada vez que hay cambios (Flow reactivo).
     * Usado en el panel de ventas del vendedor.
     *
     * Consume: vendedorId — ID del usuario vendedor.
     */
    @Query("""
        SELECT DISTINCT p.* FROM pedidos p
        INNER JOIN detalle_pedidos d ON d.pedidoId = p.id
        WHERE d.vendedorId = :vendedorId
        ORDER BY p.createdAt DESC
    """)
    fun getPedidosByVendedor(vendedorId: Long): Flow<List<Pedido>>

    /**
     * Busca un pedido por su ID local de Room.
     *
     * Consume: id — identificador primario del Pedido.
     * Devuelve: el Pedido encontrado, o null si no existe.
     */
    @Query("SELECT * FROM pedidos WHERE id = :id")
    suspend fun getPedidoById(id: Long): Pedido?

    /**
     * Devuelve todos los DetallePedido asociados a un pedido especifico.
     * Usado para mostrar el desglose de productos al ver el detalle de un pedido.
     *
     * Consume: pedidoId — ID del pedido del que se quieren los detalles.
     * Devuelve: lista de DetallePedido del pedido; puede estar vacia si no hay detalles.
     */
    @Query("SELECT * FROM detalle_pedidos WHERE pedidoId = :pedidoId")
    suspend fun getDetallesByPedido(pedidoId: Long): List<DetallePedido>

    /**
     * Inserta un nuevo pedido en la base de datos.
     *
     * Consume: pedido — entidad Pedido a insertar.
     * Devuelve: el ID local asignado por Room al pedido insertado.
     */
    @Insert
    suspend fun insertPedido(pedido: Pedido): Long

    /**
     * Inserta un DetallePedido (linea de producto) asociado a un pedido existente.
     *
     * Consume: detalle — entidad DetallePedido a insertar.
     */
    @Insert
    suspend fun insertDetalle(detalle: DetallePedido)

    /**
     * Actualiza el estado de un pedido y su marca de tiempo de modificacion.
     * Usado por vendedor y administrador para avanzar el pedido en el flujo
     * definido por EstadoPedido.
     *
     * Consume:
     *   - id: identificador del pedido a actualizar.
     *   - estado: nuevo estado del pedido (valor de cadena de EstadoPedido).
     *   - timestamp: momento del cambio de estado; por defecto el instante actual.
     */
    @Query("UPDATE pedidos SET estado = :estado, updatedAt = :timestamp WHERE id = :id")
    suspend fun actualizarEstado(id: Long, estado: String, timestamp: Long = System.currentTimeMillis())

    /**
     * Busca un pedido por su numero de orden legible (orderNumber).
     * Util para verificar si un pedido ya fue registrado antes de duplicarlo.
     *
     * Consume: orderNumber — numero de orden generado por la app (ej. "ORD-20240521-0001").
     * Devuelve: el Pedido encontrado, o null si no existe ese numero de orden.
     */
    @Query("SELECT * FROM pedidos WHERE orderNumber = :orderNumber LIMIT 1")
    suspend fun getByOrderNumber(orderNumber: String): Pedido?
}
