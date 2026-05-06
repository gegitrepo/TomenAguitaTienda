package com.example.tomenaguita.data.repository;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u001e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0086@\u00a2\u0006\u0002\u0010\u000bJ$\u0010\f\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\u000e2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00110\u0010H\u0086@\u00a2\u0006\u0002\u0010\u0012J\u0012\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\u00100\u0014J\u0018\u0010\u0015\u001a\u0004\u0018\u00010\u000e2\u0006\u0010\u0007\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\u0016J\u001c\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00110\u00102\u0006\u0010\u0018\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\u0016J\u001a\u0010\u0019\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\u00100\u00142\u0006\u0010\u001a\u001a\u00020\bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001b"}, d2 = {"Lcom/example/tomenaguita/data/repository/PedidoRepository;", "", "dao", "Lcom/example/tomenaguita/data/database/dao/PedidoDao;", "(Lcom/example/tomenaguita/data/database/dao/PedidoDao;)V", "actualizarEstado", "", "id", "", "estado", "", "(JLjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "crearPedido", "pedido", "Lcom/example/tomenaguita/data/database/entity/Pedido;", "detalles", "", "Lcom/example/tomenaguita/data/database/entity/DetallePedido;", "(Lcom/example/tomenaguita/data/database/entity/Pedido;Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllPedidos", "Lkotlinx/coroutines/flow/Flow;", "getById", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getDetalles", "pedidoId", "getPedidosByUsuario", "usuarioId", "app_debug"})
public final class PedidoRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.example.tomenaguita.data.database.dao.PedidoDao dao = null;
    
    public PedidoRepository(@org.jetbrains.annotations.NotNull()
    com.example.tomenaguita.data.database.dao.PedidoDao dao) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.example.tomenaguita.data.database.entity.Pedido>> getPedidosByUsuario(long usuarioId) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.example.tomenaguita.data.database.entity.Pedido>> getAllPedidos() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.tomenaguita.data.database.entity.Pedido> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getDetalles(long pedidoId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.tomenaguita.data.database.entity.DetallePedido>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object crearPedido(@org.jetbrains.annotations.NotNull()
    com.example.tomenaguita.data.database.entity.Pedido pedido, @org.jetbrains.annotations.NotNull()
    java.util.List<com.example.tomenaguita.data.database.entity.DetallePedido> detalles, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object actualizarEstado(long id, @org.jetbrains.annotations.NotNull()
    java.lang.String estado, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}