package com.example.tomenaguita.data.database.dao;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\r\bg\u0018\u00002\u00020\u0001J \u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0007J\u0014\u0010\b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\n0\tH\'J\u0018\u0010\f\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\r\u001a\u00020\u000eH\u00a7@\u00a2\u0006\u0002\u0010\u000fJ\u0018\u0010\u0010\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\u0011\u001a\u00020\u000eH\u00a7@\u00a2\u0006\u0002\u0010\u000fJ\u0018\u0010\u0012\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0013J\u0016\u0010\u0014\u001a\u00020\u00052\u0006\u0010\u0015\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\u0016J \u0010\u0017\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\u0011\u001a\u00020\u000e2\u0006\u0010\u0018\u001a\u00020\u000eH\u00a7@\u00a2\u0006\u0002\u0010\u0019J\u0016\u0010\u001a\u001a\u00020\u00032\u0006\u0010\u0015\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\u0016\u00a8\u0006\u001b"}, d2 = {"Lcom/example/tomenaguita/data/database/dao/UsuarioDao;", "", "desactivar", "", "id", "", "timestamp", "(JJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllUsuarios", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/example/tomenaguita/data/database/entity/Usuario;", "getByFirestoreDocId", "docId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getUsuarioByEmail", "email", "getUsuarioById", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insert", "usuario", "(Lcom/example/tomenaguita/data/database/entity/Usuario;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "login", "password", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "update", "app_debug"})
@androidx.room.Dao()
public abstract interface UsuarioDao {
    
    /**
     * Devuelve todos los usuarios activos e inactivos ordenados alfabeticamente por nombre.
     * Emite una nueva lista cada vez que hay cambios en la tabla (Flow reactivo).
     */
    @androidx.room.Query(value = "SELECT * FROM usuarios ORDER BY nombre ASC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.example.tomenaguita.data.database.entity.Usuario>> getAllUsuarios();
    
    /**
     * Busca un usuario por su ID local de Room.
     *
     * Consume: id — identificador primario del usuario.
     * Devuelve: el Usuario encontrado, o null si no existe.
     */
    @androidx.room.Query(value = "SELECT * FROM usuarios WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getUsuarioById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.tomenaguita.data.database.entity.Usuario> $completion);
    
    /**
     * Busca un usuario por su correo electronico.
     *
     * Consume: email — correo electronico del usuario.
     * Devuelve: el primer Usuario con ese correo, o null si no existe.
     */
    @androidx.room.Query(value = "SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getUsuarioByEmail(@org.jetbrains.annotations.NotNull()
    java.lang.String email, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.tomenaguita.data.database.entity.Usuario> $completion);
    
    /**
     * Verifica las credenciales del usuario para el inicio de sesion local.
     *
     * Consume:
     *  - email: correo electronico ingresado.
     *  - password: contrasena ingresada.
     * Devuelve: el Usuario si las credenciales coinciden, o null si son incorrectas.
     */
    @androidx.room.Query(value = "SELECT * FROM usuarios WHERE email = :email AND password = :password LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object login(@org.jetbrains.annotations.NotNull()
    java.lang.String email, @org.jetbrains.annotations.NotNull()
    java.lang.String password, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.tomenaguita.data.database.entity.Usuario> $completion);
    
    /**
     * Inserta un nuevo usuario o lo reemplaza si ya existe un conflicto de clave primaria.
     *
     * Consume: usuario — entidad Usuario a insertar o actualizar.
     * Devuelve: el ID local asignado por Room al usuario insertado.
     */
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insert(@org.jetbrains.annotations.NotNull()
    com.example.tomenaguita.data.database.entity.Usuario usuario, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    /**
     * Actualiza todos los campos de un usuario existente en la base de datos.
     *
     * Consume: usuario — entidad Usuario con los datos actualizados (debe tener id valido).
     */
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object update(@org.jetbrains.annotations.NotNull()
    com.example.tomenaguita.data.database.entity.Usuario usuario, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Desactiva logicamente un usuario poniendo su campo activo en 0.
     * Actualiza tambien el campo updatedAt con el timestamp actual.
     *
     * Consume:
     *  - id: identificador del usuario a desactivar.
     *  - timestamp: momento de la desactivacion; por defecto el instante actual.
     */
    @androidx.room.Query(value = "UPDATE usuarios SET activo = 0, updatedAt = :timestamp WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object desactivar(long id, long timestamp, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Busca un usuario por su ID de documento en Firestore.
     * Util para sincronizar datos entre Room y la nube sin duplicar registros.
     *
     * Consume: docId — ID del documento en Firestore.
     * Devuelve: el Usuario local vinculado a ese documento, o null si no existe.
     */
    @androidx.room.Query(value = "SELECT * FROM usuarios WHERE firestoreDocId = :docId LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getByFirestoreDocId(@org.jetbrains.annotations.NotNull()
    java.lang.String docId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.tomenaguita.data.database.entity.Usuario> $completion);
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
    }
}