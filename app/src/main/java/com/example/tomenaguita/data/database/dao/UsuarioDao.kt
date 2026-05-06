package com.example.tomenaguita.data.database.dao

import androidx.room.*
import com.example.tomenaguita.data.database.entity.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuarios ORDER BY nombre ASC")
    fun getAllUsuarios(): Flow<List<Usuario>>

    @Query("SELECT * FROM usuarios WHERE id = :id")
    suspend fun getUsuarioById(id: Long): Usuario?

    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    suspend fun getUsuarioByEmail(email: String): Usuario?

    @Query("SELECT * FROM usuarios WHERE email = :email AND password = :password LIMIT 1")
    suspend fun login(email: String, password: String): Usuario?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(usuario: Usuario): Long

    @Update
    suspend fun update(usuario: Usuario)

    @Query("UPDATE usuarios SET activo = 0, updatedAt = :timestamp WHERE id = :id")
    suspend fun desactivar(id: Long, timestamp: Long = System.currentTimeMillis())
}
