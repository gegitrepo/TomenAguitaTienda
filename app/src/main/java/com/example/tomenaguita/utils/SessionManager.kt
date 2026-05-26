package com.example.tomenaguita.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

/*
 * Gestiona la sesion del usuario autenticado usando SharedPreferences cifradas
 * (EncryptedSharedPreferences con AES-256). Almacena y recupera datos basicos
 * como el ID, rol, correo, nombre y preferencia biometrica del usuario activo.
 *
 * Maneja de forma automatica la invalidacion de la clave del Keystore que puede
 * ocurrir en ciertos dispositivos al reinstalar la app sin desinstalar previamente.
 */
class SessionManager(private val context: Context) {

    /*
     * Intenta crear las preferencias cifradas. Si la clave del Keystore fue
     * invalidada (por ejemplo en dispositivos Huawei/Honor que conservan el
     * archivo entre desinstalaciones), elimina el archivo corrupto y vuelve
     * a construir las preferencias desde cero, forzando un nuevo inicio de sesion.
     */
    private val prefs: SharedPreferences = try {
        buildPrefs(context)
    } catch (_: Exception) {
        // La clave del Keystore se invalido; se borra el archivo cifrado corrupto
        context.deleteSharedPreferences(PREFS_NAME)
        buildPrefs(context)
    }

    /**
     * Guarda los datos de sesion del usuario en las preferencias cifradas.
     *
     * Consume:
     *   - userId: identificador local (Room) del usuario.
     *   - rol: rol del usuario ("comprador", "vendedor" o "administrador").
     *   - email: correo electronico del usuario.
     *   - nombre: nombre completo del usuario.
     */
    fun saveSession(userId: Long, rol: String, email: String, nombre: String) {
        prefs.edit()
            .putLong(Constants.PREF_USER_ID,     userId)
            .putString(Constants.PREF_USER_ROL,   rol)
            .putString(Constants.PREF_USER_EMAIL,  email)
            .putString(Constants.PREF_USER_NOMBRE, nombre)
            .apply()
    }

    // Devuelve el ID local del usuario en sesion, o -1 si no hay sesion activa
    fun getUserId(): Long    = prefs.getLong(Constants.PREF_USER_ID, -1L)

    // Devuelve el rol del usuario en sesion, o null si no hay sesion activa
    fun getUserRol(): String? = prefs.getString(Constants.PREF_USER_ROL, null)

    // Devuelve el correo del usuario en sesion, o null si no hay sesion activa
    fun getUserEmail(): String? = prefs.getString(Constants.PREF_USER_EMAIL, null)

    // Devuelve el nombre del usuario en sesion, o null si no hay sesion activa
    fun getUserNombre(): String? = prefs.getString(Constants.PREF_USER_NOMBRE, null)

    // Devuelve true si el usuario habilito la autenticacion biometrica
    fun isBiometricEnabled(): Boolean = prefs.getBoolean(Constants.PREF_BIOMETRIC_ENABLED, false)

    /**
     * Activa o desactiva la autenticacion biometrica para el usuario actual.
     *
     * Consume: enabled — true para habilitar, false para deshabilitar.
     */
    fun setBiometricEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(Constants.PREF_BIOMETRIC_ENABLED, enabled).apply()
    }

    // Devuelve true si existe una sesion activa (el ID almacenado es distinto de -1)
    fun isLoggedIn(): Boolean = getUserId() != -1L

    // Elimina todos los datos de sesion almacenados (cierre de sesion)
    fun clearSession() {
        prefs.edit().clear().apply()
    }

    companion object {
        // Nombre del archivo de SharedPreferences cifradas en disco
        private const val PREFS_NAME = "tomenaguita_session"

        /**
         * Construye la instancia de EncryptedSharedPreferences usando una MasterKey
         * generada con el esquema AES-256-GCM del Keystore del sistema.
         *
         * Consume: context — contexto de Android para acceder al Keystore y al sistema de archivos.
         * Devuelve: SharedPreferences cifradas listas para usar.
         */
        private fun buildPrefs(context: Context): SharedPreferences {
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
            return EncryptedSharedPreferences.create(
                context,
                PREFS_NAME,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }
    }
}
