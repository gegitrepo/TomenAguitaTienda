package com.example.tomenaguita.data.database.dao

import androidx.room.*
import com.example.tomenaguita.data.database.entity.Producto
import kotlinx.coroutines.flow.Flow

/*
 * DAO (Data Access Object) para la tabla "productos".
 * Define todas las operaciones de acceso a datos para la entidad Producto:
 * consultas reactivas con Flow para el catalogo y el panel del vendedor,
 * busquedas puntuales, insercion, actualizacion y borrado logico.
 */
@Dao
interface ProductoDao {

    /**
     * Devuelve todos los productos disponibles y no eliminados, ordenados por nombre.
     * Emite una nueva lista cada vez que hay cambios en la tabla (Flow reactivo).
     * Usado por el catalogo visible al comprador.
     */
    @Query("SELECT * FROM productos WHERE eliminado = 0 AND disponible = 1 ORDER BY nombre ASC")
    fun getAllProductosDisponibles(): Flow<List<Producto>>

    /**
     * Devuelve todos los productos no eliminados de un vendedor especifico, ordenados por nombre.
     * Emite una nueva lista cada vez que hay cambios en la tabla (Flow reactivo).
     * Usado por el panel de gestion del vendedor.
     *
     * Consume: vendedorId — ID del usuario vendedor dueno de los productos.
     */
    @Query("SELECT * FROM productos WHERE vendedorId = :vendedorId AND eliminado = 0 ORDER BY nombre ASC")
    fun getProductosByVendedor(vendedorId: Long): Flow<List<Producto>>

    /**
     * Busca un producto no eliminado por su ID local de Room.
     *
     * Consume: id — identificador primario del producto.
     * Devuelve: el Producto encontrado, o null si no existe o fue eliminado logicamente.
     */
    @Query("SELECT * FROM productos WHERE id = :id AND eliminado = 0")
    suspend fun getProductoById(id: Long): Producto?

    /**
     * Inserta un nuevo producto o lo reemplaza si ya existe un conflicto de clave primaria.
     *
     * Consume: producto — entidad Producto a insertar o actualizar.
     * Devuelve: el ID local asignado por Room al producto insertado.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(producto: Producto): Long

    /**
     * Actualiza todos los campos de un producto existente en la base de datos.
     *
     * Consume: producto — entidad Producto con los datos actualizados (debe tener id valido).
     */
    @Update
    suspend fun update(producto: Producto)

    /**
     * Realiza un borrado logico del producto poniendo su campo eliminado en 1.
     * El producto deja de aparecer en consultas pero su historial en pedidos se conserva.
     * Actualiza tambien el campo updatedAt con el timestamp actual.
     *
     * Consume:
     *   - id: identificador del producto a eliminar logicamente.
     *   - timestamp: momento del borrado; por defecto el instante actual.
     */
    @Query("UPDATE productos SET eliminado = 1, updatedAt = :timestamp WHERE id = :id")
    suspend fun softDelete(id: Long, timestamp: Long = System.currentTimeMillis())

    /**
     * Busca un producto por su ID de documento en Firestore.
     * Util para sincronizar datos entre Room y la nube sin duplicar registros.
     *
     * Consume: docId — ID del documento en Firestore.
     * Devuelve: el Producto local vinculado a ese documento, o null si no existe.
     */
    @Query("SELECT * FROM productos WHERE firestoreDocId = :docId LIMIT 1")
    suspend fun getByFirestoreDocId(docId: String): Producto?
}
