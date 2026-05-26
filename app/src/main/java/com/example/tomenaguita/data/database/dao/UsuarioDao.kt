package com.example.tomenaguita.data.database.dao

import androidx.room.*
import com.example.tomenaguita.data.database.entity.Usuario
import kotlinx.coroutines.flow.Flow

/*
 * DAO (Data Access Object) para la tabla "usuarios".
 * Define todas las operaciones de acceso a datos para la entidad Usuario:
 * consultas reactivas con Flow, busquedas puntuales, autenticacion local,
 * insercion, actualizacion y desactivacion logica.
 */
@Dao
interface UsuarioDao {

    /**
     * Devuelve todos los usuarios activos e inactivos ordenados alfabeticamente por nombre.
     * Emite una nueva lista cada vez que hay cambios en la tabla (Flow reactivo).
     */
    @Query("SELECT * FROM usuarios ORDER BY nombre ASC")
    fun getAllUsuarios(): Flow<List<Usuario>>

    /**
     * Busca un usuario por su ID local de Room.
     *
     * Consume: id — identificador primario del usuario.
     * Devuelve: el Usuario encontrado, o null si no existe.
     */
    @Query("SELECT * FROM usuarios WHERE id = :id")
    suspend fun getUsuarioById(id: Long): Usuario?

    /**
     * Busca un usuario por su correo electronico.
     *
     * Consume: email — correo electronico del usuario.
     * Devuelve: el primer Usuario con ese correo, o null si no existe.
     */
    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    suspend fun getUsuarioByEmail(email: String): Usuario?

    /**
     * Verifica las credenciales del usuario para el inicio de sesion local.
     *
     * Consume:
     *   - email: correo electronico ingresado.
     *   - password: contrasena ingresada.
     * Devuelve: el Usuario si las credenciales coinciden, o null si son incorrectas.
     */
    @Query("SELECT * FROM usuarios WHERE email = :email AND password = :password LIMIT 1")
    suspend fun login(email: String, password: String): Usuario?

    /**
     * Inserta un nuevo usuario o lo reemplaza si ya existe un conflicto de clave primaria.
     *
     * Consume: usuario — entidad Usuario a insertar o actualizar.
     * Devuelve: el ID local asignado por Room al usuario insertado.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(usuario: Usuario): Long

    /**
     * Actualiza todos los campos de un usuario existente en la base de datos.
     *
     * Consume: usuario — entidad Usuario con los datos actualizados (debe tener id valido).
     */
    @Update
    suspend fun update(usuario: Usuario)

    /**
     * Desactiva logicamente un usuario poniendo su campo activo en 0.
     * Actualiza tambien el campo updatedAt con el timestamp actual.
     *
     * Consume:
     *   - id: identificador del usuario a desactivar.
     *   - timestamp: momento de la desactivacion; por defecto el instante actual.
     */
    @Query("UPDATE usuarios SET activo = 0, updatedAt = :timestamp WHERE id = :id")
    suspend fun desactivar(id: Long, timestamp: Long = System.currentTimeMillis())

    /**
     * Busca un usuario por su ID de documento en Firestore.
     * Util para sincronizar datos entre Room y la nube sin duplicar registros.
     *
     * Consume: docId — ID del documento en Firestore.
     * Devuelve: el Usuario local vinculado a ese documento, o null si no existe.
     */
    @Query("SELECT * FROM usuarios WHERE firestoreDocId = :docId LIMIT 1")
    suspend fun getByFirestoreDocId(docId: String): Usuario?
}
