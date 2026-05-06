package com.example.tomenaguita.utils

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SessionManager(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs = EncryptedSharedPreferences.create(
        context,
        "tomenaguita_session",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveSession(userId: Long, rol: String, email: String, nombre: String) {
        prefs.edit()
            .putLong(Constants.PREF_USER_ID, userId)
            .putString(Constants.PREF_USER_ROL, rol)
            .putString(Constants.PREF_USER_EMAIL, email)
            .putString(Constants.PREF_USER_NOMBRE, nombre)
            .apply()
    }

    fun getUserId(): Long = prefs.getLong(Constants.PREF_USER_ID, -1L)
    fun getUserRol(): String? = prefs.getString(Constants.PREF_USER_ROL, null)
    fun getUserEmail(): String? = prefs.getString(Constants.PREF_USER_EMAIL, null)
    fun getUserNombre(): String? = prefs.getString(Constants.PREF_USER_NOMBRE, null)
    fun isBiometricEnabled(): Boolean = prefs.getBoolean(Constants.PREF_BIOMETRIC_ENABLED, false)

    fun setBiometricEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(Constants.PREF_BIOMETRIC_ENABLED, enabled).apply()
    }

    fun isLoggedIn(): Boolean = getUserId() != -1L

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}
