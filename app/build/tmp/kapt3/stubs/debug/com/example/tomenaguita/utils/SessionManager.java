package com.example.tomenaguita.utils;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\n\u0018\u0000 \u00192\u00020\u0001:\u0001\u0019B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u0007\u001a\u00020\bJ\b\u0010\t\u001a\u0004\u0018\u00010\nJ\u0006\u0010\u000b\u001a\u00020\fJ\b\u0010\r\u001a\u0004\u0018\u00010\nJ\b\u0010\u000e\u001a\u0004\u0018\u00010\nJ\u0006\u0010\u000f\u001a\u00020\u0010J\u0006\u0010\u0011\u001a\u00020\u0010J&\u0010\u0012\u001a\u00020\b2\u0006\u0010\u0013\u001a\u00020\f2\u0006\u0010\u0014\u001a\u00020\n2\u0006\u0010\u0015\u001a\u00020\n2\u0006\u0010\u0016\u001a\u00020\nJ\u000e\u0010\u0017\u001a\u00020\b2\u0006\u0010\u0018\u001a\u00020\u0010R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001a"}, d2 = {"Lcom/example/tomenaguita/utils/SessionManager;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "prefs", "Landroid/content/SharedPreferences;", "clearSession", "", "getUserEmail", "", "getUserId", "", "getUserNombre", "getUserRol", "isBiometricEnabled", "", "isLoggedIn", "saveSession", "userId", "rol", "email", "nombre", "setBiometricEnabled", "enabled", "Companion", "app_debug"})
public final class SessionManager {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final android.content.SharedPreferences prefs = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String PREFS_NAME = "tomenaguita_session";
    @org.jetbrains.annotations.NotNull()
    public static final com.example.tomenaguita.utils.SessionManager.Companion Companion = null;
    
    public SessionManager(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    /**
     * Guarda los datos de sesion del usuario en las preferencias cifradas.
     *
     * Consume:
     *  - userId: identificador local (Room) del usuario.
     *  - rol: rol del usuario ("comprador", "vendedor" o "administrador").
     *  - email: correo electronico del usuario.
     *  - nombre: nombre completo del usuario.
     */
    public final void saveSession(long userId, @org.jetbrains.annotations.NotNull()
    java.lang.String rol, @org.jetbrains.annotations.NotNull()
    java.lang.String email, @org.jetbrains.annotations.NotNull()
    java.lang.String nombre) {
    }
    
    public final long getUserId() {
        return 0L;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getUserRol() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getUserEmail() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getUserNombre() {
        return null;
    }
    
    public final boolean isBiometricEnabled() {
        return false;
    }
    
    /**
     * Activa o desactiva la autenticacion biometrica para el usuario actual.
     *
     * Consume: enabled — true para habilitar, false para deshabilitar.
     */
    public final void setBiometricEnabled(boolean enabled) {
    }
    
    public final boolean isLoggedIn() {
        return false;
    }
    
    public final void clearSession() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2 = {"Lcom/example/tomenaguita/utils/SessionManager$Companion;", "", "()V", "PREFS_NAME", "", "buildPrefs", "Landroid/content/SharedPreferences;", "context", "Landroid/content/Context;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        /**
         * Construye la instancia de EncryptedSharedPreferences usando una MasterKey
         * generada con el esquema AES-256-GCM del Keystore del sistema.
         *
         * Consume: context — contexto de Android para acceder al Keystore y al sistema de archivos.
         * Devuelve: SharedPreferences cifradas listas para usar.
         */
        private final android.content.SharedPreferences buildPrefs(android.content.Context context) {
            return null;
        }
    }
}