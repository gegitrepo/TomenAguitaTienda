package com.example.tomenaguita.data.database.dao;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0004\bg\u0018\u00002\u00020\u0001J\u0014\u0010\u0002\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u0003H\'J\u0018\u0010\u0006\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0007\u001a\u00020\bH\u00a7@\u00a2\u0006\u0002\u0010\tJ\u0018\u0010\n\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u000b\u001a\u00020\fH\u00a7@\u00a2\u0006\u0002\u0010\rJ\u001c\u0010\u000e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\u0006\u0010\u000f\u001a\u00020\fH\'J\u0016\u0010\u0010\u001a\u00020\f2\u0006\u0010\u0011\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0012J \u0010\u0013\u001a\u00020\u00142\u0006\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\u0015\u001a\u00020\fH\u00a7@\u00a2\u0006\u0002\u0010\u0016J\u0016\u0010\u0017\u001a\u00020\u00142\u0006\u0010\u0011\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0012\u00a8\u0006\u0018"}, d2 = {"Lcom/example/tomenaguita/data/database/dao/ProductoDao;", "", "getAllProductosDisponibles", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/example/tomenaguita/data/database/entity/Producto;", "getByFirestoreDocId", "docId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getProductoById", "id", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getProductosByVendedor", "vendedorId", "insert", "producto", "(Lcom/example/tomenaguita/data/database/entity/Producto;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "softDelete", "", "timestamp", "(JJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "update", "app_debug"})
@androidx.room.Dao()
public abstract interface ProductoDao {
    
    /**
     * Devuelve todos los productos disponibles y no eliminados, ordenados por nombre.
     * Emite una nueva lista cada vez que hay cambios en la tabla (Flow reactivo).
     * Usado por el catalogo visible al comprador.
     */
    @androidx.room.Query(value = "SELECT * FROM productos WHERE eliminado = 0 AND disponible = 1 ORDER BY nombre ASC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.example.tomenaguita.data.database.entity.Producto>> getAllProductosDisponibles();
    
    /**
     * Devuelve todos los productos no eliminados de un vendedor especifico, ordenados por nombre.
     * Emite una nueva lista cada vez que hay cambios en la tabla (Flow reactivo).
     * Usado por el panel de gestion del vendedor.
     *
     * Consume: vendedorId — ID del usuario vendedor dueno de los productos.
     */
    @androidx.room.Query(value = "SELECT * FROM productos WHERE vendedorId = :vendedorId AND eliminado = 0 ORDER BY nombre ASC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.example.tomenaguita.data.database.entity.Producto>> getProductosByVendedor(long vendedorId);
    
    /**
     * Busca un producto no eliminado por su ID local de Room.
     *
     * Consume: id — identificador primario del producto.
     * Devuelve: el Producto encontrado, o null si no existe o fue eliminado logicamente.
     */
    @androidx.room.Query(value = "SELECT * FROM productos WHERE id = :id AND eliminado = 0")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getProductoById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.tomenaguita.data.database.entity.Producto> $completion);
    
    /**
     * Inserta un nuevo producto o lo reemplaza si ya existe un conflicto de clave primaria.
     *
     * Consume: producto — entidad Producto a insertar o actualizar.
     * Devuelve: el ID local asignado por Room al producto insertado.
     */
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insert(@org.jetbrains.annotations.NotNull()
    com.example.tomenaguita.data.database.entity.Producto producto, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    /**
     * Actualiza todos los campos de un producto existente en la base de datos.
     *
     * Consume: producto — entidad Producto con los datos actualizados (debe tener id valido).
     */
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object update(@org.jetbrains.annotations.NotNull()
    com.example.tomenaguita.data.database.entity.Producto producto, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Realiza un borrado logico del producto poniendo su campo eliminado en 1.
     * El producto deja de aparecer en consultas pero su historial en pedidos se conserva.
     * Actualiza tambien el campo updatedAt con el timestamp actual.
     *
     * Consume:
     *  - id: identificador del producto a eliminar logicamente.
     *  - timestamp: momento del borrado; por defecto el instante actual.
     */
    @androidx.room.Query(value = "UPDATE productos SET eliminado = 1, updatedAt = :timestamp WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object softDelete(long id, long timestamp, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Busca un producto por su ID de documento en Firestore.
     * Util para sincronizar datos entre Room y la nube sin duplicar registros.
     *
     * Consume: docId — ID del documento en Firestore.
     * Devuelve: el Producto local vinculado a ese documento, o null si no existe.
     */
    @androidx.room.Query(value = "SELECT * FROM productos WHERE firestoreDocId = :docId LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getByFirestoreDocId(@org.jetbrains.annotations.NotNull()
    java.lang.String docId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.tomenaguita.data.database.entity.Producto> $completion);
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
    }
}