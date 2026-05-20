package com.example.tomenaguita.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tomenaguita.data.database.AppDatabase
import com.example.tomenaguita.data.database.entity.Usuario
import com.example.tomenaguita.data.repository.FirebaseAuthRepository
import com.example.tomenaguita.data.repository.UsuarioRepository
import com.example.tomenaguita.utils.SessionManager
import com.example.tomenaguita.data.repository.CarritoRepository
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import java.security.MessageDigest

class AuthViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = UsuarioRepository(AppDatabase.getInstance(app).usuarioDao())
    private val carritoRepo = CarritoRepository(AppDatabase.getInstance(app).carritoDao())
    private val firebaseAuthRepo = FirebaseAuthRepository()
    private val session = SessionManager(app)

    private val _loginResult = MutableLiveData<Result<Usuario>>()
    val loginResult: LiveData<Result<Usuario>> = _loginResult

    private val _registerResult = MutableLiveData<Result<Long>>()
    val registerResult: LiveData<Result<Long>> = _registerResult

    private val _resetResult = MutableLiveData<Result<Unit>>()
    val resetResult: LiveData<Result<Unit>> = _resetResult

    init {
        viewModelScope.launch {
            try { firebaseAuthRepo.seedDemoDataIfEmpty() } catch (_: Exception) { }
        }
    }

    fun login(email: String, password: String) = viewModelScope.launch {
        // 1. Autenticar con Firebase Auth
        val firebaseResult = firebaseAuthRepo.login(email, password)
        if (firebaseResult.isFailure) {
            _loginResult.value = Result.failure(
                Exception(mapFirebaseAuthError(firebaseResult.exceptionOrNull()?.message))
            )
            return@launch
        }

        val firebaseUser = firebaseResult.getOrNull()!!

        // 2. Obtener datos del usuario desde Firestore
        val data = firebaseAuthRepo.getUserData(firebaseUser.uid)
        if (data == null) {
            firebaseAuthRepo.logout()
            _loginResult.value = Result.failure(Exception("Perfil de usuario no encontrado. Contacta al administrador"))
            return@launch
        }

        val rol = data["rol"] as? String ?: "comprador"
        val nombre = data["nombre"] as? String ?: email.substringBefore("@")
        val telefono = data["telefono"] as? String ?: ""
        val activo = data["activo"] as? Boolean ?: true

        if (!activo) {
            firebaseAuthRepo.logout()
            _loginResult.value = Result.failure(Exception("Usuario inactivo. Contacta al administrador"))
            return@launch
        }

        // 3. Upsert en Room como cache local
        var localUser = repo.getByEmail(email)
        if (localUser == null) {
            val newUser = Usuario(
                nombre = nombre,
                email = email,
                telefono = telefono,
                password = sha256(password),
                rol = rol,
                activo = if (activo) 1 else 0,
                fotoUrl = data["fotoUrl"] as? String,
                direccion = data["direccion"] as? String
            )
            val id = repo.insert(newUser)
            localUser = newUser.copy(id = id)
        }

        // 4. Limpiar carrito previo de este usuario en Room (ítems de sesiones anteriores)
        carritoRepo.vaciar(localUser.id)

        // 5. Guardar sesion local
        session.saveSession(localUser.id, rol, email, nombre)
        _loginResult.value = Result.success(localUser)
    }

    fun register(nombre: String, email: String, telefono: String, direccion: String, password: String) =
        viewModelScope.launch {
            // 1. Crear en Firebase Auth
            val firebaseResult = firebaseAuthRepo.register(email, password)
            if (firebaseResult.isFailure) {
                val msg = mapFirebaseAuthError(firebaseResult.exceptionOrNull()?.message)
                _registerResult.value = Result.failure(Exception(msg))
                return@launch
            }

            val firebaseUser = firebaseResult.getOrNull()!!

            // 2. Crear documento en Firestore
            val data = mapOf(
                "nombre" to nombre,
                "email" to email,
                "telefono" to telefono,
                "direccion" to direccion,
                "rol" to "comprador",
                "activo" to true,
                "biometricEnabled" to false,
                "createdAt" to Timestamp.now(),
                "updatedAt" to Timestamp.now()
            )
            firebaseAuthRepo.saveUserData(firebaseUser.uid, data)

            // 3. Insertar en Room como cache local
            val usuario = Usuario(
                nombre = nombre,
                email = email,
                telefono = telefono,
                password = sha256(password),
                rol = "comprador",
                direccion = direccion.takeIf { it.isNotEmpty() }
            )
            val id = repo.insert(usuario)

            // 4. Guardar sesión (mismo paso que en login, rol fijo "comprador")
            session.saveSession(id, "comprador", email, nombre)

            _registerResult.value = Result.success(id)
        }

    fun sendPasswordReset(email: String) = viewModelScope.launch {
        _resetResult.value = firebaseAuthRepo.sendPasswordReset(email)
    }

    private fun sha256(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    private fun mapFirebaseAuthError(message: String?): String = when {
        message == null -> "Error de autenticación"
        "no user record" in message || "user-not-found" in message -> "Correo o contraseña incorrectos"
        "password is invalid" in message || "wrong-password" in message -> "Correo o contraseña incorrectos"
        "email address is already in use" in message || "email-already-in-use" in message -> "El correo ya está registrado"
        "network" in message.lowercase() -> "Sin conexión a internet. Verifica tu red"
        else -> "Error de autenticación. Intenta de nuevo"
    }
}
