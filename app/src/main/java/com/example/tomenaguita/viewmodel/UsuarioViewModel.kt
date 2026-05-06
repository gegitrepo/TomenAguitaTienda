package com.example.tomenaguita.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.tomenaguita.data.database.AppDatabase
import com.example.tomenaguita.data.database.entity.Usuario
import com.example.tomenaguita.data.repository.UsuarioRepository
import kotlinx.coroutines.launch

class UsuarioViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = UsuarioRepository(AppDatabase.getInstance(app).usuarioDao())

    val todosLosUsuarios = repo.getAllUsuarios().asLiveData()

    private val _usuarioActual = MutableLiveData<Usuario?>()
    val usuarioActual: LiveData<Usuario?> = _usuarioActual

    private val _operationResult = MutableLiveData<Result<Unit>>()
    val operationResult: LiveData<Result<Unit>> = _operationResult

    fun cargarUsuario(id: Long) = viewModelScope.launch {
        _usuarioActual.value = repo.getById(id)
    }

    fun actualizarPerfil(usuario: Usuario) = viewModelScope.launch {
        try {
            repo.update(usuario)
            _usuarioActual.value = usuario
            _operationResult.value = Result.success(Unit)
        } catch (e: Exception) {
            _operationResult.value = Result.failure(e)
        }
    }

    fun crearUsuario(usuario: Usuario) = viewModelScope.launch {
        try {
            repo.insert(usuario)
            _operationResult.value = Result.success(Unit)
        } catch (e: Exception) {
            _operationResult.value = Result.failure(e)
        }
    }

    fun desactivarUsuario(id: Long) = viewModelScope.launch {
        try {
            repo.desactivar(id)
            _operationResult.value = Result.success(Unit)
        } catch (e: Exception) {
            _operationResult.value = Result.failure(e)
        }
    }
}
