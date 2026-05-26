package com.example.tomenaguita.data.database.dao

import androidx.room.*
import com.example.tomenaguita.data.database.entity.CarritoItem
import kotlinx.coroutines.flow.Flow

/*
 * DAO (Data Access Object) para la tabla "carrito".
 * Define todas las operaciones de acceso a datos para la entidad CarritoItem:
 * consulta reactiva del contenido y conteo del carrito de un usuario,
 * insercion, actualizacion y eliminacion de items individuales o totales.
 */
@Dao
interface CarritoDao {

    /**
     * Devuelve todos los items del carrito de un usuario ordenados por fecha de insercion.
     * Emite una nueva lista cada vez que hay cambios en la tabla (Flow reactivo).
     *
     * Consume: usuarioId — ID del usuario dueno del carrito.
     */
    @Query("SELECT * FROM carrito WHERE usuarioId = :usuarioId ORDER BY createdAt ASC")
    fun getCarritoByUsuario(usuarioId: Long): Flow<List<CarritoItem>>

    /**
     * Devuelve el numero total de items distintos en el carrito del usuario.
     * Emite el nuevo conteo cada vez que se agrega o elimina un item (Flow reactivo).
     * Util para mostrar el badge del icono del carrito en la navegacion.
     *
     * Consume: usuarioId — ID del usuario dueno del carrito.
     */
    @Query("SELECT COUNT(*) FROM carrito WHERE usuarioId = :usuarioId")
    fun getCarritoCount(usuarioId: Long): Flow<Int>

    /**
     * Inserta un nuevo item en el carrito o lo reemplaza si ya existe
     * un conflicto de clave primaria.
     *
     * Consume: item — entidad CarritoItem a insertar.
     * Devuelve: el ID local asignado por Room al item insertado.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CarritoItem): Long

    /**
     * Actualiza los datos de un item existente en el carrito (por ejemplo, la cantidad).
     *
     * Consume: item — entidad CarritoItem con los datos actualizados (debe tener id valido).
     */
    @Update
    suspend fun update(item: CarritoItem)

    /**
     * Elimina un item especifico del carrito por su ID.
     *
     * Consume: id — identificador primario del CarritoItem a eliminar.
     */
    @Query("DELETE FROM carrito WHERE id = :id")
    suspend fun delete(id: Long)

    /**
     * Elimina todos los items del carrito de un usuario.
     * Se usa al confirmar un pedido o al limpiar el carrito manualmente.
     *
     * Consume: usuarioId — ID del usuario cuyo carrito se vaciara.
     */
    @Query("DELETE FROM carrito WHERE usuarioId = :usuarioId")
    suspend fun vaciarCarrito(usuarioId: Long)

    /**
     * Busca un item del carrito por su ID local de Room.
     *
     * Consume: id — identificador primario del CarritoItem.
     * Devuelve: el CarritoItem encontrado, o null si no existe.
     */
    @Query("SELECT * FROM carrito WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): CarritoItem?

    /**
     * Busca si un producto especifico ya esta en el carrito de un usuario.
     * Util para decidir si se debe insertar un nuevo item o actualizar la cantidad del existente.
     *
     * Consume:
     *   - usuarioId: ID del usuario dueno del carrito.
     *   - productoId: ID del producto a buscar en el carrito.
     * Devuelve: el CarritoItem si ya existe ese producto en el carrito, o null si no.
     */
    @Query("SELECT * FROM carrito WHERE usuarioId = :usuarioId AND productoId = :productoId LIMIT 1")
    suspend fun getByUsuarioAndProducto(usuarioId: Long, productoId: Long): CarritoItem?
}
