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

// ViewModel de gestión de usuarios. Permite al administrador listar, crear,
// actualizar y desactivar usuarios. Sincroniza en tiempo real Firestore con el
// caché local de Room y expone LiveData para que los fragments observen los cambios.
class UsuarioViewModel(app: Application) : AndroidViewModel(app) {

    // Repositorio local Room para operaciones CRUD sobre la tabla de usuarios
    private val roomRepo = UsuarioRepository(AppDatabase.getInstance(app).usuarioDao())

    // Repositorio Firestore para mantener los usuarios sincronizados en la nube
    private val firestoreRepo = FirestoreUsuarioRepository()

    // Lista reactiva de todos los usuarios almacenados en Room; se actualiza automáticamente
    val todosLosUsuarios = roomRepo.getAllUsuarios().asLiveData()

    // Usuario seleccionado o cargado para edición o visualización de detalle
    private val _usuarioActual = MutableLiveData<Usuario?>()
    val usuarioActual: LiveData<Usuario?> = _usuarioActual

    // Null = sin evento pendiente. El observer debe llamar clearOperationResult() al procesarlo.
    private val _operationResult = MutableLiveData<Result<Unit>?>()
    val operationResult: LiveData<Result<Unit>?> = _operationResult

    // Limpia el resultado de operación para evitar que el observer lo procese más de una vez
    fun clearOperationResult() { _operationResult.value = null }

    // Al crear el ViewModel, inicia la sincronización en tiempo real de Firestore hacia Room.
    // Inserta usuarios nuevos y actualiza los existentes si cambió su estado o docId.
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

    // Carga un usuario específico desde Room y lo publica en _usuarioActual.
    // Parámetros: id es el identificador local del usuario en Room.
    fun cargarUsuario(id: Long) = viewModelScope.launch {
        _usuarioActual.value = roomRepo.getById(id)
    }

    // Actualiza el perfil de un usuario tanto en Firestore como en Room.
    // Parámetros: usuario es el objeto con los datos modificados.
    // Publica en _operationResult el resultado (éxito o error).
    fun actualizarPerfil(usuario: Usuario) = viewModelScope.launch {
        try {
            // Actualizar en Firestore si el usuario tiene docId asignado
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
            // Actualizar en Room con la marca de tiempo actual
            roomRepo.update(usuario.copy(updatedAt = System.currentTimeMillis()))
            _usuarioActual.value = usuario
            _operationResult.value = Result.success(Unit)
        } catch (e: Exception) {
            _operationResult.value = Result.failure(e)
        }
    }

    // Crea un nuevo usuario en Firestore y lo replica en Room como caché local.
    // Parámetros: nombre, email, telefono, password, rol del nuevo usuario y direccion opcional.
    // Publica en _operationResult el resultado de la operación.
    fun crearUsuario(
        nombre: String, email: String, telefono: String,
        password: String, rol: String, direccion: String = ""
    ) = viewModelScope.launch {
            try {
                // Insertar en Firestore y obtener el ID del documento creado
                val firestoreDocId = firestoreRepo.insert(nombre, email, telefono, password, rol, direccion)
                roomRepo.insert(Usuario(
                    nombre = nombre,
                    email = email,
                    telefono = telefono,
                    // La contraseña no se almacena en Room en este flujo de creación por admin
                    password = "",
                    rol = rol,
                    direccion = direccion.takeIf { it.isNotBlank() },
                    firestoreDocId = firestoreDocId
                ))
                _operationResult.value = Result.success(Unit)
            } catch (e: Exception) {
                _operationResult.value = Result.failure(e)
            }
        }

    // Marca un usuario como inactivo (borrado lógico) tanto en Firestore como en Room.
    // Parámetros: id es el identificador local del usuario en Room.
    // Publica en _operationResult el resultado de la operación.
    fun desactivarUsuario(id: Long) = viewModelScope.launch {
        try {
            // Desactivar en Firestore usando el docId del usuario
            roomRepo.getById(id)?.firestoreDocId?.let { firestoreRepo.desactivar(it) }
            // Desactivar en Room localmente
            roomRepo.desactivar(id)
            _operationResult.value = Result.success(Unit)
        } catch (e: Exception) {
            _operationResult.value = Result.failure(e)
        }
    }
}
