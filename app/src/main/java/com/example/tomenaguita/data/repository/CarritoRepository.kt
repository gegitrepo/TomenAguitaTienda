package com.example.tomenaguita.data.repository

import com.example.tomenaguita.data.database.dao.CarritoDao
import com.example.tomenaguita.data.database.entity.CarritoItem
import kotlinx.coroutines.flow.Flow

/*
 * CarritoRepository
 *
 * Repositorio local del carrito de compras que actua como fuente de verdad para la UI.
 * Encapsula todas las operaciones sobre la base de datos Room (SQLite local)
 * a traves del CarritoDao. Los ViewModels consumen los items del carrito via Flow,
 * lo que permite que la pantalla del carrito se actualice reactivamente ante
 * cualquier cambio (agregar, eliminar, actualizar cantidad).
 *
 * Patron: Room es la fuente de verdad local; Firestore (FirestoreCarritoRepository)
 * es la contraparte en la nube que persiste el carrito y lo sincroniza entre dispositivos.
 */
class CarritoRepository(private val dao: CarritoDao) {

    // Devuelve un Flow con la lista de items del carrito para un usuario especifico,
    // identificado por su ID local de Room. La UI se actualiza automaticamente ante cambios.
    fun getCarrito(usuarioId: Long): Flow<List<CarritoItem>> = dao.getCarritoByUsuario(usuarioId)

    // Devuelve un Flow con el numero total de items distintos en el carrito del usuario.
    // Util para mostrar el badge (contador) en el icono del carrito.
    fun getCount(usuarioId: Long): Flow<Int> = dao.getCarritoCount(usuarioId)

    // Inserta un nuevo item en el carrito en Room y devuelve el ID local generado.
    suspend fun insert(item: CarritoItem): Long = dao.insert(item)

    // Actualiza un item existente del carrito en Room (por ejemplo, para cambiar la cantidad).
    suspend fun update(item: CarritoItem) = dao.update(item)

    // Elimina un item del carrito en Room por su ID local.
    suspend fun delete(id: Long) = dao.delete(id)

    // Elimina todos los items del carrito de un usuario en Room.
    // Se usa al confirmar un pedido o al cerrar sesion.
    suspend fun vaciar(usuarioId: Long) = dao.vaciarCarrito(usuarioId)

    // Busca y devuelve un item del carrito por su ID local de Room. Devuelve null si no existe.
    suspend fun getById(id: Long): CarritoItem? = dao.getById(id)

    // Busca un item del carrito filtrando por usuario y producto al mismo tiempo.
    // Util para verificar si un producto ya esta en el carrito antes de insertarlo de nuevo.
    // Devuelve null si no se encuentra ninguna coincidencia.
    suspend fun getByUsuarioAndProducto(usuarioId: Long, productoId: Long): CarritoItem? =
        dao.getByUsuarioAndProducto(usuarioId, productoId)
}
