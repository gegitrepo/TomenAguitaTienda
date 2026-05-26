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

// ViewModel de autenticación. Gestiona el inicio de sesión, registro y recuperación
// de contraseña coordinando Firebase Auth, Firestore y el caché local de Room.
// Expone LiveData con el resultado de cada operación para que los fragments reaccionen.
class AuthViewModel(app: Application) : AndroidViewModel(app) {

    // Repositorio local Room para consultar y guardar usuarios en la BD del dispositivo
    private val repo = UsuarioRepository(AppDatabase.getInstance(app).usuarioDao())

    // Repositorio local Room para limpiar el carrito al iniciar sesión
    private val carritoRepo = CarritoRepository(AppDatabase.getInstance(app).carritoDao())

    // Repositorio de Firebase Auth y Firestore para autenticación en la nube
    private val firebaseAuthRepo = FirebaseAuthRepository()

    // Gestiona la sesión activa del usuario en SharedPreferences
    private val session = SessionManager(app)

    // Resultado del intento de login; contiene el Usuario autenticado o el error producido
    private val _loginResult = MutableLiveData<Result<Usuario>>()
    val loginResult: LiveData<Result<Usuario>> = _loginResult

    // Resultado del registro; contiene el ID local generado por Room o el error producido
    private val _registerResult = MutableLiveData<Result<Long>>()
    val registerResult: LiveData<Result<Long>> = _registerResult

    // Resultado del envío del correo de recuperación de contraseña
    private val _resetResult = MutableLiveData<Result<Unit>>()
    val resetResult: LiveData<Result<Unit>> = _resetResult

    // Al inicializarse, carga datos de demostración en Firestore si está vacío
    init {
        viewModelScope.launch {
            try { firebaseAuthRepo.seedDemoDataIfEmpty() } catch (_: Exception) { }
        }
    }

    // Autentica al usuario contra Firebase Auth, obtiene su perfil de Firestore,
    // lo guarda en Room como caché y persiste la sesión local.
    // Parámetros: email y password ingresados por el usuario.
    // Publica en _loginResult el usuario autenticado o un error descriptivo.
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

        // Verificar que la cuenta esté habilitada por el administrador
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

    // Registra un nuevo usuario: crea la cuenta en Firebase Auth, guarda el perfil
    // en Firestore con rol "comprador" e inserta el registro en Room.
    // Parámetros: datos del formulario de registro (nombre, email, telefono, direccion, password).
    // Publica en _registerResult el ID local del nuevo usuario o un error.
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
                // HARDCODED: todo registro desde la app pública es siempre comprador
                rol = "comprador",
                direccion = direccion.takeIf { it.isNotEmpty() }
            )
            val id = repo.insert(usuario)

            // 4. Guardar sesión (mismo paso que en login, rol fijo "comprador")
            session.saveSession(id, "comprador", email, nombre)

            _registerResult.value = Result.success(id)
        }

    // Envía un correo de recuperación de contraseña a través de Firebase Auth.
    // Parámetros: email al que se enviará el enlace de recuperación.
    // Publica en _resetResult el resultado de la operación (éxito o error).
    fun sendPasswordReset(email: String) = viewModelScope.launch {
        _resetResult.value = firebaseAuthRepo.sendPasswordReset(email)
    }

    // Genera el hash SHA-256 de la contraseña para almacenarla en Room sin texto plano.
    // Parámetros: input es la contraseña en texto plano.
    // Devuelve la representación hexadecimal del hash.
    private fun sha256(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    // Traduce los mensajes de error de Firebase Auth al español para mostrarlos al usuario.
    // Parámetros: message es el mensaje de error original de Firebase (puede ser nulo).
    // Devuelve un mensaje amigable en español listo para mostrarse en la UI.
    private fun mapFirebaseAuthError(message: String?): String = when {
        message == null -> "Error de autenticación"
        "no user record" in message || "user-not-found" in message -> "Correo o contraseña incorrectos"
        "password is invalid" in message || "wrong-password" in message -> "Correo o contraseña incorrectos"
        "email address is already in use" in message || "email-already-in-use" in message -> "El correo ya está registrado"
        "network" in message.lowercase() -> "Sin conexión a internet. Verifica tu red"
        else -> "Error de autenticación. Intenta de nuevo"
    }
}
