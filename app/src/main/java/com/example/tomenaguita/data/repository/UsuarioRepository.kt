package com.example.tomenaguita.data.repository

import com.example.tomenaguita.data.database.dao.UsuarioDao
import com.example.tomenaguita.data.database.entity.Usuario
import kotlinx.coroutines.flow.Flow

/*
 * UsuarioRepository
 *
 * Repositorio local de usuarios que actua como fuente de verdad para la UI.
 * Encapsula todas las operaciones sobre la base de datos Room (SQLite local)
 * a traves del UsuarioDao. La UI y los ViewModels consumen datos desde Room
 * via Flow o funciones suspendidas, garantizando respuestas inmediatas sin
 * depender de la red.
 *
 * Patron: Room es la fuente de verdad local; Firestore (FirestoreUsuarioRepository)
 * es la contraparte en la nube que sincroniza los datos entre dispositivos.
 */
class UsuarioRepository(private val dao: UsuarioDao) {

    // Devuelve un Flow con la lista completa de usuarios almacenados en Room.
    // La UI se actualiza automaticamente cada vez que cambian los datos locales.
    fun getAllUsuarios(): Flow<List<Usuario>> = dao.getAllUsuarios()

    // Busca y devuelve un usuario por su ID local de Room. Devuelve null si no existe.
    suspend fun getById(id: Long): Usuario? = dao.getUsuarioById(id)

    // Busca y devuelve un usuario por su direccion de correo electronico. Devuelve null si no existe.
    suspend fun getByEmail(email: String): Usuario? = dao.getUsuarioByEmail(email)

    // Valida las credenciales del usuario comparando email y hash de contrasena.
    // Devuelve el Usuario si las credenciales son correctas, null en caso contrario.
    suspend fun login(email: String, passwordHash: String): Usuario? =
        dao.login(email, passwordHash)

    // Inserta un nuevo usuario en Room y devuelve el ID local generado por Room.
    suspend fun insert(usuario: Usuario): Long = dao.insert(usuario)

    // Actualiza los datos de un usuario existente en Room.
    suspend fun update(usuario: Usuario) = dao.update(usuario)

    // Marca un usuario como inactivo en Room sin eliminarlo fisicamente (borrado logico).
    // Recibe el ID local del usuario a desactivar.
    suspend fun desactivar(id: Long) = dao.desactivar(id)

    // Busca un usuario por su ID de documento en Firestore (firestoreDocId).
    // Util para sincronizar registros de Firestore con su contraparte local en Room.
    // Devuelve null si no se encuentra ninguna coincidencia.
    suspend fun getByFirestoreDocId(docId: String): Usuario? = dao.getByFirestoreDocId(docId)
}
