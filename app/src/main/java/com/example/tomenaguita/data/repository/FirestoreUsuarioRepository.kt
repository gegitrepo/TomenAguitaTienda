package com.example.tomenaguita.data.repository

import com.example.tomenaguita.data.database.entity.Usuario
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirestoreUsuarioRepository {

    private val auth = FirebaseAuth.getInstance()
    private val col = FirebaseFirestore.getInstance().collection("usuarios")

    fun getAllUsuarios(): Flow<List<Usuario>> = callbackFlow {
        val listener = col.addSnapshotListener { snap, err ->
            if (err != null) { close(err); return@addSnapshotListener }
            trySend(snap?.documents?.mapNotNull { it.toUsuario() } ?: emptyList())
        }
        awaitClose { listener.remove() }
    }

    suspend fun insert(nombre: String, email: String, telefono: String, password: String, rol: String): String {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val uid = result.user?.uid ?: throw Exception("Error al crear usuario en Firebase Auth")
        col.document(uid).set(mapOf(
            "nombre" to nombre,
            "email" to email,
            "telefono" to telefono,
            "rol" to rol,
            "activo" to true,
            "biometricEnabled" to false,
            "createdAt" to Timestamp.now(),
            "updatedAt" to Timestamp.now()
        )).await()
        return uid
    }

    suspend fun update(docId: String, campos: Map<String, Any>) {
        col.document(docId).update(campos + mapOf("updatedAt" to Timestamp.now())).await()
    }

    suspend fun desactivar(docId: String) = update(docId, mapOf("activo" to false))

    private fun DocumentSnapshot.toUsuario(): Usuario? {
        val nombre = getString("nombre") ?: return null
        val email = getString("email") ?: return null
        return Usuario(
            id = 0L,
            nombre = nombre,
            email = email,
            password = "",
            telefono = getString("telefono") ?: "",
            rol = getString("rol") ?: "comprador",
            activo = if (getBoolean("activo") == true) 1 else 0,
            fotoUrl = getString("fotoUrl"),
            direccion = getString("direccion"),
            firestoreDocId = id
        )
    }
}
