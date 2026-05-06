package com.example.tomenaguita.data.repository

import com.example.tomenaguita.data.database.dao.UsuarioDao
import com.example.tomenaguita.data.database.entity.Usuario
import kotlinx.coroutines.flow.Flow

class UsuarioRepository(private val dao: UsuarioDao) {

    fun getAllUsuarios(): Flow<List<Usuario>> = dao.getAllUsuarios()

    suspend fun getById(id: Long): Usuario? = dao.getUsuarioById(id)

    suspend fun getByEmail(email: String): Usuario? = dao.getUsuarioByEmail(email)

    suspend fun login(email: String, passwordHash: String): Usuario? =
        dao.login(email, passwordHash)

    suspend fun insert(usuario: Usuario): Long = dao.insert(usuario)

    suspend fun update(usuario: Usuario) = dao.update(usuario)

    suspend fun desactivar(id: Long) = dao.desactivar(id)
}
