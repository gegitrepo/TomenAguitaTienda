package com.example.tomenaguita.data.database.dao;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\bg\u0018\u00002\u00020\u0001J(\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\tJ\u0014\u0010\n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\f0\u000bH\'J\u001c\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000f0\f2\u0006\u0010\u0010\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0011J\u0018\u0010\u0012\u001a\u0004\u0018\u00010\r2\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0011J\u001c\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\f0\u000b2\u0006\u0010\u0014\u001a\u00020\u0005H\'J\u0016\u0010\u0015\u001a\u00020\u00032\u0006\u0010\u0016\u001a\u00020\u000fH\u00a7@\u00a2\u0006\u0002\u0010\u0017J\u0016\u0010\u0018\u001a\u00020\u00052\u0006\u0010\u0019\u001a\u00020\rH\u00a7@\u00a2\u0006\u0002\u0010\u001a\u00a8\u0006\u001b"}, d2 = {"Lcom/example/tomenaguita/data/database/dao/PedidoDao;", "", "actualizarEstado", "", "id", "", "estado", "", "timestamp", "(JLjava/lang/String;JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllPedidos", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/example/tomenaguita/data/database/entity/Pedido;", "getDetallesByPedido", "Lcom/example/tomenaguita/data/database/entity/DetallePedido;", "pedidoId", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getPedidoById", "getPedidosByUsuario", "usuarioId", "insertDetalle", "detalle", "(Lcom/example/tomenaguita/data/database/entity/DetallePedido;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertPedido", "pedido", "(Lcom/example/tomenaguita/data/database/entity/Pedido;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.room.Dao()
public abstract interface PedidoDao {
    
    @androidx.room.Query(value = "SELECT * FROM pedidos WHERE usuarioId = :usuarioId ORDER BY createdAt DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.example.tomenaguita.data.database.entity.Pedido>> getPedidosByUsuario(long usuarioId);
    
    @androidx.room.Query(value = "SELECT * FROM pedidos ORDER BY createdAt DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.example.tomenaguita.data.database.entity.Pedido>> getAllPedidos();
    
    @androidx.room.Query(value = "SELECT * FROM pedidos WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getPedidoById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.tomenaguita.data.database.entity.Pedido> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM detalle_pedidos WHERE pedidoId = :pedidoId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getDetallesByPedido(long pedidoId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.tomenaguita.data.database.entity.DetallePedido>> $completion);
    
    @androidx.room.Insert()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertPedido(@org.jetbrains.annotations.NotNull()
    com.example.tomenaguita.data.database.entity.Pedido pedido, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Insert()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertDetalle(@org.jetbrains.annotations.NotNull()
    com.example.tomenaguita.data.database.entity.DetallePedido detalle, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE pedidos SET estado = :estado, updatedAt = :timestamp WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object actualizarEstado(long id, @org.jetbrains.annotations.NotNull()
    java.lang.String estado, long timestamp, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
    }
}