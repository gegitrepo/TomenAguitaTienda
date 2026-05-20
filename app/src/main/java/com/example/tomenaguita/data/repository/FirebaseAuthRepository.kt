package com.example.tomenaguita.data.repository

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun login(email: String, password: String): Result<FirebaseUser> = runCatching {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        result.user ?: throw Exception("Usuario no encontrado")
    }

    suspend fun register(email: String, password: String): Result<FirebaseUser> = runCatching {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        result.user ?: throw Exception("Error al crear el usuario")
    }

    suspend fun sendPasswordReset(email: String): Result<Unit> = runCatching {
        auth.sendPasswordResetEmail(email).await()
    }

    fun logout() = auth.signOut()

    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    suspend fun getUserData(uid: String): Map<String, Any>? {
        val snap = firestore.collection("usuarios").document(uid).get().await()
        return if (snap.exists()) snap.data else null
    }

    suspend fun saveUserData(uid: String, data: Map<String, Any>) {
        firestore.collection("usuarios").document(uid)
            .set(data, SetOptions.merge()).await()
    }

    /** Crea los 3 usuarios demo en Firebase Auth y Firestore si la coleccion esta vacia. */
    suspend fun seedDemoDataIfEmpty() {
        val snap = firestore.collection("usuarios").limit(1).get().await()
        if (!snap.isEmpty) return

        val demoUsers = listOf(
            Triple(
                "admin@tomenaguita.com", "Admin123!", mapOf(
                    "nombre" to "Administrador",
                    "email" to "admin@tomenaguita.com",
                    "telefono" to "3001234567",
                    "rol" to "administrador",
                    "activo" to true,
                    "biometricEnabled" to false,
                    "createdAt" to Timestamp.now(),
                    "updatedAt" to Timestamp.now()
                )
            ),
            Triple(
                "vendedor@tomenaguita.com", "Vendedor123!", mapOf(
                    "nombre" to "Vendedor Demo",
                    "email" to "vendedor@tomenaguita.com",
                    "telefono" to "3007654321",
                    "rol" to "vendedor",
                    "activo" to true,
                    "biometricEnabled" to false,
                    "createdAt" to Timestamp.now(),
                    "updatedAt" to Timestamp.now()
                )
            ),
            Triple(
                "comprador@tomenaguita.com", "Comprador123!", mapOf(
                    "nombre" to "Comprador Demo",
                    "email" to "comprador@tomenaguita.com",
                    "telefono" to "3009876543",
                    "rol" to "comprador",
                    "activo" to true,
                    "biometricEnabled" to false,
                    "createdAt" to Timestamp.now(),
                    "updatedAt" to Timestamp.now()
                )
            )
        )

        for ((email, password, data) in demoUsers) {
            try {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                val uid = result.user?.uid ?: continue
                firestore.collection("usuarios").document(uid).set(data).await()
            } catch (_: Exception) { }
        }
        // Dejar sin sesion activa tras el seed
        auth.signOut()
    }
}
