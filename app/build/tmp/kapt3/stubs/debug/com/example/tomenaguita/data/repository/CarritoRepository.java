package com.example.tomenaguita.data.repository;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\tJ\u001a\u0010\n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\f0\u000b2\u0006\u0010\u000e\u001a\u00020\bJ\u0014\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00100\u000b2\u0006\u0010\u000e\u001a\u00020\bJ\u0016\u0010\u0011\u001a\u00020\b2\u0006\u0010\u0012\u001a\u00020\rH\u0086@\u00a2\u0006\u0002\u0010\u0013J\u0016\u0010\u0014\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\rH\u0086@\u00a2\u0006\u0002\u0010\u0013J\u0016\u0010\u0015\u001a\u00020\u00062\u0006\u0010\u000e\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\tR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0016"}, d2 = {"Lcom/example/tomenaguita/data/repository/CarritoRepository;", "", "dao", "Lcom/example/tomenaguita/data/database/dao/CarritoDao;", "(Lcom/example/tomenaguita/data/database/dao/CarritoDao;)V", "delete", "", "id", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getCarrito", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/example/tomenaguita/data/database/entity/CarritoItem;", "usuarioId", "getCount", "", "insert", "item", "(Lcom/example/tomenaguita/data/database/entity/CarritoItem;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "update", "vaciar", "app_debug"})
public final class CarritoRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.example.tomenaguita.data.database.dao.CarritoDao dao = null;
    
    public CarritoRepository(@org.jetbrains.annotations.NotNull()
    com.example.tomenaguita.data.database.dao.CarritoDao dao) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.example.tomenaguita.data.database.entity.CarritoItem>> getCarrito(long usuarioId) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.Integer> getCount(long usuarioId) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object insert(@org.jetbrains.annotations.NotNull()
    com.example.tomenaguita.data.database.entity.CarritoItem item, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object update(@org.jetbrains.annotations.NotNull()
    com.example.tomenaguita.data.database.entity.CarritoItem item, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object delete(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object vaciar(long usuarioId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}