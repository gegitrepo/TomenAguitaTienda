package com.example.tomenaguita.data.repository

import com.example.tomenaguita.data.database.entity.Usuario
import com.example.tomenaguita.data.model.Rol
import com.example.tomenaguita.utils.Constants
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/*
 * FirestoreUsuarioRepository
 *
 * Repositorio de usuarios en la nube. Gestiona la lectura y escritura de
 * documentos en la coleccion "usuarios" de Firestore (Google Cloud).
 *
 * Es la contraparte en la nube de UsuarioRepository (Room). Mientras Room
 * almacena usuarios localmente para respuesta inmediata en la UI, este
 * repositorio garantiza que los datos persistan en la nube y se sincronicen
 * entre dispositivos y sesiones.
 *
 * Tambien usa FirebaseAuth para la creacion y autenticacion de cuentas.
 */
class FirestoreUsuarioRepository {

    // Instancia de FirebaseAuth para crear y autenticar usuarios en Firebase.
    private val auth = FirebaseAuth.getInstance()

    // Referencia a la coleccion de usuarios en Firestore.
    private val col = FirebaseFirestore.getInstance().collection(Constants.FS_USUARIOS)

    /*
     * Devuelve un Flow que emite la lista completa de usuarios en tiempo real.
     * Usa un listener de Firestore para detectar cambios en la coleccion y
     * propagar las actualizaciones automaticamente a los colectores del Flow.
     * El listener se cancela cuando el Flow deja de ser observado (awaitClose).
     */
    fun getAllUsuarios(): Flow<List<Usuario>> = callbackFlow {
        val listener = col.addSnapshotListener { snap, err ->
            if (err != null) { close(err); return@addSnapshotListener }
            trySend(snap?.documents?.mapNotNull { it.toUsuario() } ?: emptyList())
        }
        awaitClose { listener.remove() }
    }

    /*
     * Crea un nuevo usuario en Firebase Auth y luego guarda su perfil completo
     * en Firestore bajo el documento con el UID de Firebase como identificador.
     * Devuelve el UID del usuario creado en Firebase Auth.
     *
     * Parametros:
     *   nombre    - nombre completo del usuario
     *   email     - correo electronico (usado como credencial en Firebase Auth)
     *   telefono  - numero de telefono de contacto
     *   password  - contrasena en texto plano (Firebase Auth la gestiona de forma segura)
     *   rol       - rol del usuario en la app ("comprador", "vendedor", "admin")
     *   direccion - direccion de entrega predeterminada (opcional)
     */
    suspend fun insert(
        nombre: String, email: String, telefono: String,
        password: String, rol: String, direccion: String = ""
    ): String {
        // Crear la cuenta en Firebase Auth y obtener el UID generado.
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val uid = result.user?.uid ?: throw Exception("Error al crear usuario en Firebase Auth")

        // Guardar el perfil del usuario en Firestore usando el UID como ID del documento.
        col.document(uid).set(mapOf(
            "nombre" to nombre,
            "email" to email,
            "telefono" to telefono,
            "rol" to rol,
            "activo" to true,
            "biometricEnabled" to false,
            "direccion" to direccion,
            "createdAt" to Timestamp.now(),
            "updatedAt" to Timestamp.now()
        )).await()
        return uid
    }

    /*
     * Actualiza campos especificos del documento de un usuario en Firestore.
     * Agrega automaticamente el campo "updatedAt" con la fecha y hora actuales.
     *
     * Parametros:
     *   docId  - ID del documento en Firestore (equivale al UID de Firebase Auth)
     *   campos - mapa de campos a actualizar con sus nuevos valores
     */
    suspend fun update(docId: String, campos: Map<String, Any>) {
        col.document(docId).update(campos + mapOf("updatedAt" to Timestamp.now())).await()
    }

    // Marca un usuario como inactivo en Firestore sin eliminar su documento.
    // Recibe el ID del documento en Firestore (UID de Firebase Auth).
    suspend fun desactivar(docId: String) = update(docId, mapOf("activo" to false))

    /*
     * Funcion de extension privada que convierte un DocumentSnapshot de Firestore
     * en una entidad Usuario de Room. Devuelve null si faltan campos obligatorios
     * (nombre o email), evitando que datos incompletos lleguen a la base de datos local.
     */
    private fun DocumentSnapshot.toUsuario(): Usuario? {
        val nombre = getString("nombre") ?: return null
        val email = getString("email") ?: return null
        return Usuario(
            id = 0L,
            nombre = nombre,
            email = email,
            password = "",
            telefono = getString("telefono") ?: "",
            rol = getString("rol") ?: Rol.COMPRADOR.valor,
            activo = if (getBoolean("activo") == true) 1 else 0,
            fotoUrl = getString("fotoUrl"),
            direccion = getString("direccion"),
            firestoreDocId = id
        )
    }
}
