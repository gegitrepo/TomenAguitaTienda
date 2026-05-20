package com.example.tomenaguita.viewmodel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u000b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020 J\u0012\u0010\"\u001a\u00020 2\b\u0010#\u001a\u0004\u0018\u00010 H\u0002J.\u0010$\u001a\u00020\u001e2\u0006\u0010%\u001a\u00020 2\u0006\u0010\u001f\u001a\u00020 2\u0006\u0010&\u001a\u00020 2\u0006\u0010\'\u001a\u00020 2\u0006\u0010!\u001a\u00020 J\u000e\u0010(\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020 J\u0010\u0010)\u001a\u00020 2\u0006\u0010*\u001a\u00020 H\u0002R\u001a\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u001d\u0010\u0015\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u00070\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0014R\u000e\u0010\u0017\u001a\u00020\u0018X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0019\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u00070\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0014R\u000e\u0010\u001b\u001a\u00020\u001cX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006+"}, d2 = {"Lcom/example/tomenaguita/viewmodel/AuthViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "app", "Landroid/app/Application;", "(Landroid/app/Application;)V", "_loginResult", "Landroidx/lifecycle/MutableLiveData;", "Lkotlin/Result;", "Lcom/example/tomenaguita/data/database/entity/Usuario;", "_registerResult", "", "_resetResult", "", "carritoRepo", "Lcom/example/tomenaguita/data/repository/CarritoRepository;", "firebaseAuthRepo", "Lcom/example/tomenaguita/data/repository/FirebaseAuthRepository;", "loginResult", "Landroidx/lifecycle/LiveData;", "getLoginResult", "()Landroidx/lifecycle/LiveData;", "registerResult", "getRegisterResult", "repo", "Lcom/example/tomenaguita/data/repository/UsuarioRepository;", "resetResult", "getResetResult", "session", "Lcom/example/tomenaguita/utils/SessionManager;", "login", "Lkotlinx/coroutines/Job;", "email", "", "password", "mapFirebaseAuthError", "message", "register", "nombre", "telefono", "direccion", "sendPasswordReset", "sha256", "input", "app_debug"})
public final class AuthViewModel extends androidx.lifecycle.AndroidViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.example.tomenaguita.data.repository.UsuarioRepository repo = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.tomenaguita.data.repository.CarritoRepository carritoRepo = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.tomenaguita.data.repository.FirebaseAuthRepository firebaseAuthRepo = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.tomenaguita.utils.SessionManager session = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<kotlin.Result<com.example.tomenaguita.data.database.entity.Usuario>> _loginResult = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<kotlin.Result<com.example.tomenaguita.data.database.entity.Usuario>> loginResult = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<kotlin.Result<java.lang.Long>> _registerResult = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<kotlin.Result<java.lang.Long>> registerResult = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<kotlin.Result<kotlin.Unit>> _resetResult = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<kotlin.Result<kotlin.Unit>> resetResult = null;
    
    public AuthViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application app) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<kotlin.Result<com.example.tomenaguita.data.database.entity.Usuario>> getLoginResult() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<kotlin.Result<java.lang.Long>> getRegisterResult() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<kotlin.Result<kotlin.Unit>> getResetResult() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job login(@org.jetbrains.annotations.NotNull()
    java.lang.String email, @org.jetbrains.annotations.NotNull()
    java.lang.String password) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job register(@org.jetbrains.annotations.NotNull()
    java.lang.String nombre, @org.jetbrains.annotations.NotNull()
    java.lang.String email, @org.jetbrains.annotations.NotNull()
    java.lang.String telefono, @org.jetbrains.annotations.NotNull()
    java.lang.String direccion, @org.jetbrains.annotations.NotNull()
    java.lang.String password) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job sendPasswordReset(@org.jetbrains.annotations.NotNull()
    java.lang.String email) {
        return null;
    }
    
    private final java.lang.String sha256(java.lang.String input) {
        return null;
    }
    
    private final java.lang.String mapFirebaseAuthError(java.lang.String message) {
        return null;
    }
}