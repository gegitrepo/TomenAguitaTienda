package com.example.tomenaguita.data.database.dao;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0018\u0010\u0007\u001a\u0004\u0018\u00010\b2\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J \u0010\t\u001a\u0004\u0018\u00010\b2\u0006\u0010\n\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\fJ\u001c\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u000f0\u000e2\u0006\u0010\n\u001a\u00020\u0005H\'J\u0016\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00110\u000e2\u0006\u0010\n\u001a\u00020\u0005H\'J\u0016\u0010\u0012\u001a\u00020\u00052\u0006\u0010\u0013\u001a\u00020\bH\u00a7@\u00a2\u0006\u0002\u0010\u0014J\u0016\u0010\u0015\u001a\u00020\u00032\u0006\u0010\u0013\u001a\u00020\bH\u00a7@\u00a2\u0006\u0002\u0010\u0014J\u0016\u0010\u0016\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006\u00a8\u0006\u0017"}, d2 = {"Lcom/example/tomenaguita/data/database/dao/CarritoDao;", "", "delete", "", "id", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getById", "Lcom/example/tomenaguita/data/database/entity/CarritoItem;", "getByUsuarioAndProducto", "usuarioId", "productoId", "(JJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getCarritoByUsuario", "Lkotlinx/coroutines/flow/Flow;", "", "getCarritoCount", "", "insert", "item", "(Lcom/example/tomenaguita/data/database/entity/CarritoItem;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "update", "vaciarCarrito", "app_debug"})
@androidx.room.Dao()
public abstract interface CarritoDao {
    
    /**
     * Devuelve todos los items del carrito de un usuario ordenados por fecha de insercion.
     * Emite una nueva lista cada vez que hay cambios en la tabla (Flow reactivo).
     *
     * Consume: usuarioId — ID del usuario dueno del carrito.
     */
    @androidx.room.Query(value = "SELECT * FROM carrito WHERE usuarioId = :usuarioId ORDER BY createdAt ASC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.example.tomenaguita.data.database.entity.CarritoItem>> getCarritoByUsuario(long usuarioId);
    
    /**
     * Devuelve el numero total de items distintos en el carrito del usuario.
     * Emite el nuevo conteo cada vez que se agrega o elimina un item (Flow reactivo).
     * Util para mostrar el badge del icono del carrito en la navegacion.
     *
     * Consume: usuarioId — ID del usuario dueno del carrito.
     */
    @androidx.room.Query(value = "SELECT COUNT(*) FROM carrito WHERE usuarioId = :usuarioId")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.lang.Integer> getCarritoCount(long usuarioId);
    
    /**
     * Inserta un nuevo item en el carrito o lo reemplaza si ya existe
     * un conflicto de clave primaria.
     *
     * Consume: item — entidad CarritoItem a insertar.
     * Devuelve: el ID local asignado por Room al item insertado.
     */
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insert(@org.jetbrains.annotations.NotNull()
    com.example.tomenaguita.data.database.entity.CarritoItem item, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    /**
     * Actualiza los datos de un item existente en el carrito (por ejemplo, la cantidad).
     *
     * Consume: item — entidad CarritoItem con los datos actualizados (debe tener id valido).
     */
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object update(@org.jetbrains.annotations.NotNull()
    com.example.tomenaguita.data.database.entity.CarritoItem item, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Elimina un item especifico del carrito por su ID.
     *
     * Consume: id — identificador primario del CarritoItem a eliminar.
     */
    @androidx.room.Query(value = "DELETE FROM carrito WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object delete(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Elimina todos los items del carrito de un usuario.
     * Se usa al confirmar un pedido o al limpiar el carrito manualmente.
     *
     * Consume: usuarioId — ID del usuario cuyo carrito se vaciara.
     */
    @androidx.room.Query(value = "DELETE FROM carrito WHERE usuarioId = :usuarioId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object vaciarCarrito(long usuarioId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Busca un item del carrito por su ID local de Room.
     *
     * Consume: id — identificador primario del CarritoItem.
     * Devuelve: el CarritoItem encontrado, o null si no existe.
     */
    @androidx.room.Query(value = "SELECT * FROM carrito WHERE id = :id LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.tomenaguita.data.database.entity.CarritoItem> $completion);
    
    /**
     * Busca si un producto especifico ya esta en el carrito de un usuario.
     * Util para decidir si se debe insertar un nuevo item o actualizar la cantidad del existente.
     *
     * Consume:
     *  - usuarioId: ID del usuario dueno del carrito.
     *  - productoId: ID del producto a buscar en el carrito.
     * Devuelve: el CarritoItem si ya existe ese producto en el carrito, o null si no.
     */
    @androidx.room.Query(value = "SELECT * FROM carrito WHERE usuarioId = :usuarioId AND productoId = :productoId LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getByUsuarioAndProducto(long usuarioId, long productoId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.tomenaguita.data.database.entity.CarritoItem> $completion);
}