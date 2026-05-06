package com.example.tomenaguita.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tomenaguita.data.database.AppDatabase
import com.example.tomenaguita.data.database.entity.Usuario
import com.example.tomenaguita.data.repository.UsuarioRepository
import kotlinx.coroutines.launch
import java.security.MessageDigest

class AuthViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = UsuarioRepository(AppDatabase.getInstance(app).usuarioDao())

    private val _loginResult = MutableLiveData<Result<Usuario>>()
    val loginResult: LiveData<Result<Usuario>> = _loginResult

    private val _registerResult = MutableLiveData<Result<Long>>()
    val registerResult: LiveData<Result<Long>> = _registerResult

    fun login(email: String, password: String) = viewModelScope.launch {
        val hash = sha256(password)
        val usuario = repo.login(email, hash)
        _loginResult.value = if (usuario != null) Result.success(usuario)
        else Result.failure(Exception("Correo o contraseña incorrectos"))
    }

    fun register(nombre: String, email: String, telefono: String, password: String) =
        viewModelScope.launch {
            val existing = repo.getByEmail(email)
            if (existing != null) {
                _registerResult.value = Result.failure(Exception("El correo ya está registrado"))
                return@launch
            }
            val usuario = Usuario(
                nombre = nombre,
                email = email,
                telefono = telefono,
                password = sha256(password),
                rol = "comprador"
            )
            val id = repo.insert(usuario)
            _registerResult.value = Result.success(id)
        }

    private fun sha256(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
