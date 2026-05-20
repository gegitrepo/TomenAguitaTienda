package com.example.tomenaguita.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.tomenaguita.data.database.AppDatabase
import com.example.tomenaguita.data.database.entity.Usuario
import com.example.tomenaguita.data.repository.FirestoreUsuarioRepository
import com.example.tomenaguita.data.repository.UsuarioRepository
import kotlinx.coroutines.launch

class UsuarioViewModel(app: Application) : AndroidViewModel(app) {

    private val roomRepo = UsuarioRepository(AppDatabase.getInstance(app).usuarioDao())
    private val firestoreRepo = FirestoreUsuarioRepository()

    val todosLosUsuarios = roomRepo.getAllUsuarios().asLiveData()

    private val _usuarioActual = MutableLiveData<Usuario?>()
    val usuarioActual: LiveData<Usuario?> = _usuarioActual

    // Null = sin evento pendiente. El observer debe llamar clearOperationResult() al procesarlo.
    private val _operationResult = MutableLiveData<Result<Unit>?>()
    val operationResult: LiveData<Result<Unit>?> = _operationResult

    fun clearOperationResult() { _operationResult.value = null }

    init {
        viewModelScope.launch {
            try {
                firestoreRepo.getAllUsuarios().collect { firestoreUsers ->
                    firestoreUsers.forEach { user ->
                        val existing = roomRepo.getByEmail(user.email)
                        if (existing == null) {
                            roomRepo.insert(user)
                        } else if (existing.firestoreDocId != user.firestoreDocId || existing.activo != user.activo) {
                            roomRepo.update(existing.copy(
                                nombre = user.nombre,
                                rol = user.rol,
                                activo = user.activo,
                                fotoUrl = user.fotoUrl,
                                firestoreDocId = user.firestoreDocId,
                                updatedAt = System.currentTimeMillis()
                            ))
                        }
                    }
                }
            } catch (_: Exception) { }
        }
    }

    fun cargarUsuario(id: Long) = viewModelScope.launch {
        _usuarioActual.value = roomRepo.getById(id)
    }

    fun actualizarPerfil(usuario: Usuario) = viewModelScope.launch {
        try {
            usuario.firestoreDocId?.let { docId ->
                firestoreRepo.update(docId, mapOf(
                    "nombre" to usuario.nombre,
                    "telefono" to usuario.telefono,
                    "rol" to usuario.rol,
                    "activo" to (usuario.activo == 1),
                    "fotoUrl" to (usuario.fotoUrl ?: ""),
                    "direccion" to (usuario.direccion ?: "")
                ))
            }
            roomRepo.update(usuario.copy(updatedAt = System.currentTimeMillis()))
            _usuarioActual.value = usuario
            _operationResult.value = Result.success(Unit)
        } catch (e: Exception) {
            _operationResult.value = Result.failure(e)
        }
    }

    fun crearUsuario(nombre: String, email: String, telefono: String, password: String, rol: String) =
        viewModelScope.launch {
            try {
                val firestoreDocId = firestoreRepo.insert(nombre, email, telefono, password, rol)
                roomRepo.insert(Usuario(
                    nombre = nombre,
                    email = email,
                    telefono = telefono,
                    password = "",
                    rol = rol,
                    firestoreDocId = firestoreDocId
                ))
                _operationResult.value = Result.success(Unit)
            } catch (e: Exception) {
                _operationResult.value = Result.failure(e)
            }
        }

    fun desactivarUsuario(id: Long) = viewModelScope.launch {
        try {
            roomRepo.getById(id)?.firestoreDocId?.let { firestoreRepo.desactivar(it) }
            roomRepo.desactivar(id)
            _operationResult.value = Result.success(Unit)
        } catch (e: Exception) {
            _operationResult.value = Result.failure(e)
        }
    }
}
