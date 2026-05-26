package com.example.tomenaguita.data.database.dao;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u000e\bg\u0018\u00002\u00020\u0001J(\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\tJ\u0014\u0010\n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\f0\u000bH\'J\u0018\u0010\u000e\u001a\u0004\u0018\u00010\r2\u0006\u0010\u000f\u001a\u00020\u0007H\u00a7@\u00a2\u0006\u0002\u0010\u0010J\u001c\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00120\f2\u0006\u0010\u0013\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0014J\u0018\u0010\u0015\u001a\u0004\u0018\u00010\r2\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0014J\u001c\u0010\u0016\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\f0\u000b2\u0006\u0010\u0017\u001a\u00020\u0005H\'J\u001c\u0010\u0018\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\f0\u000b2\u0006\u0010\u0019\u001a\u00020\u0005H\'J\u0016\u0010\u001a\u001a\u00020\u00032\u0006\u0010\u001b\u001a\u00020\u0012H\u00a7@\u00a2\u0006\u0002\u0010\u001cJ\u0016\u0010\u001d\u001a\u00020\u00052\u0006\u0010\u001e\u001a\u00020\rH\u00a7@\u00a2\u0006\u0002\u0010\u001f\u00a8\u0006 "}, d2 = {"Lcom/example/tomenaguita/data/database/dao/PedidoDao;", "", "actualizarEstado", "", "id", "", "estado", "", "timestamp", "(JLjava/lang/String;JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllPedidos", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/example/tomenaguita/data/database/entity/Pedido;", "getByOrderNumber", "orderNumber", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getDetallesByPedido", "Lcom/example/tomenaguita/data/database/entity/DetallePedido;", "pedidoId", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getPedidoById", "getPedidosByUsuario", "usuarioId", "getPedidosByVendedor", "vendedorId", "insertDetalle", "detalle", "(Lcom/example/tomenaguita/data/database/entity/DetallePedido;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertPedido", "pedido", "(Lcom/example/tomenaguita/data/database/entity/Pedido;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.room.Dao()
public abstract interface PedidoDao {
    
    /**
     * Devuelve todos los pedidos de un comprador especifico, del mas reciente al mas antiguo.
     * Emite una nueva lista cada vez que hay cambios en la tabla (Flow reactivo).
     * Usado en el historial de compras del comprador.
     *
     * Consume: usuarioId — ID del usuario comprador.
     */
    @androidx.room.Query(value = "SELECT * FROM pedidos WHERE usuarioId = :usuarioId ORDER BY createdAt DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.example.tomenaguita.data.database.entity.Pedido>> getPedidosByUsuario(long usuarioId);
    
    /**
     * Devuelve todos los pedidos de todos los usuarios, del mas reciente al mas antiguo.
     * Emite una nueva lista cada vez que hay cambios en la tabla (Flow reactivo).
     * Usado en el panel de administrador.
     */
    @androidx.room.Query(value = "SELECT * FROM pedidos ORDER BY createdAt DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.example.tomenaguita.data.database.entity.Pedido>> getAllPedidos();
    
    /**
     * Devuelve los pedidos que contienen al menos un producto del vendedor indicado.
     * Usa un JOIN con detalle_pedidos para filtrar por vendedorId y DISTINCT para
     * evitar duplicados cuando el pedido tiene varios productos del mismo vendedor.
     * Emite una nueva lista cada vez que hay cambios (Flow reactivo).
     * Usado en el panel de ventas del vendedor.
     *
     * Consume: vendedorId — ID del usuario vendedor.
     */
    @androidx.room.Query(value = "\n        SELECT DISTINCT p.* FROM pedidos p\n        INNER JOIN detalle_pedidos d ON d.pedidoId = p.id\n        WHERE d.vendedorId = :vendedorId\n        ORDER BY p.createdAt DESC\n    ")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.example.tomenaguita.data.database.entity.Pedido>> getPedidosByVendedor(long vendedorId);
    
    /**
     * Busca un pedido por su ID local de Room.
     *
     * Consume: id — identificador primario del Pedido.
     * Devuelve: el Pedido encontrado, o null si no existe.
     */
    @androidx.room.Query(value = "SELECT * FROM pedidos WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getPedidoById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.tomenaguita.data.database.entity.Pedido> $completion);
    
    /**
     * Devuelve todos los DetallePedido asociados a un pedido especifico.
     * Usado para mostrar el desglose de productos al ver el detalle de un pedido.
     *
     * Consume: pedidoId — ID del pedido del que se quieren los detalles.
     * Devuelve: lista de DetallePedido del pedido; puede estar vacia si no hay detalles.
     */
    @androidx.room.Query(value = "SELECT * FROM detalle_pedidos WHERE pedidoId = :pedidoId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getDetallesByPedido(long pedidoId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.tomenaguita.data.database.entity.DetallePedido>> $completion);
    
    /**
     * Inserta un nuevo pedido en la base de datos.
     *
     * Consume: pedido — entidad Pedido a insertar.
     * Devuelve: el ID local asignado por Room al pedido insertado.
     */
    @androidx.room.Insert()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertPedido(@org.jetbrains.annotations.NotNull()
    com.example.tomenaguita.data.database.entity.Pedido pedido, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    /**
     * Inserta un DetallePedido (linea de producto) asociado a un pedido existente.
     *
     * Consume: detalle — entidad DetallePedido a insertar.
     */
    @androidx.room.Insert()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertDetalle(@org.jetbrains.annotations.NotNull()
    com.example.tomenaguita.data.database.entity.DetallePedido detalle, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Actualiza el estado de un pedido y su marca de tiempo de modificacion.
     * Usado por vendedor y administrador para avanzar el pedido en el flujo
     * definido por EstadoPedido.
     *
     * Consume:
     *  - id: identificador del pedido a actualizar.
     *  - estado: nuevo estado del pedido (valor de cadena de EstadoPedido).
     *  - timestamp: momento del cambio de estado; por defecto el instante actual.
     */
    @androidx.room.Query(value = "UPDATE pedidos SET estado = :estado, updatedAt = :timestamp WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object actualizarEstado(long id, @org.jetbrains.annotations.NotNull()
    java.lang.String estado, long timestamp, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Busca un pedido por su numero de orden legible (orderNumber).
     * Util para verificar si un pedido ya fue registrado antes de duplicarlo.
     *
     * Consume: orderNumber — numero de orden generado por la app (ej. "ORD-20240521-0001").
     * Devuelve: el Pedido encontrado, o null si no existe ese numero de orden.
     */
    @androidx.room.Query(value = "SELECT * FROM pedidos WHERE orderNumber = :orderNumber LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getByOrderNumber(@org.jetbrains.annotations.NotNull()
    java.lang.String orderNumber, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.tomenaguita.data.database.entity.Pedido> $completion);
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
    }
}