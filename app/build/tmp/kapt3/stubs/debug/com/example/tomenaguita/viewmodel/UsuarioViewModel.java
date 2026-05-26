package com.example.tomenaguita.viewmodel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\nJ\u000e\u0010\u001b\u001a\u00020\u00192\u0006\u0010\u001c\u001a\u00020\u001dJ\u0006\u0010\u001e\u001a\u00020\bJ8\u0010\u001f\u001a\u00020\u00192\u0006\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020!2\u0006\u0010#\u001a\u00020!2\u0006\u0010$\u001a\u00020!2\u0006\u0010%\u001a\u00020!2\b\b\u0002\u0010&\u001a\u00020!J\u000e\u0010\'\u001a\u00020\u00192\u0006\u0010\u001c\u001a\u00020\u001dR\u001c\u0010\u0005\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\b\u0018\u00010\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\t\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001f\u0010\r\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\b\u0018\u00010\u00070\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u00140\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0010R\u0019\u0010\u0016\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0010\u00a8\u0006("}, d2 = {"Lcom/example/tomenaguita/viewmodel/UsuarioViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "app", "Landroid/app/Application;", "(Landroid/app/Application;)V", "_operationResult", "Landroidx/lifecycle/MutableLiveData;", "Lkotlin/Result;", "", "_usuarioActual", "Lcom/example/tomenaguita/data/database/entity/Usuario;", "firestoreRepo", "Lcom/example/tomenaguita/data/repository/FirestoreUsuarioRepository;", "operationResult", "Landroidx/lifecycle/LiveData;", "getOperationResult", "()Landroidx/lifecycle/LiveData;", "roomRepo", "Lcom/example/tomenaguita/data/repository/UsuarioRepository;", "todosLosUsuarios", "", "getTodosLosUsuarios", "usuarioActual", "getUsuarioActual", "actualizarPerfil", "Lkotlinx/coroutines/Job;", "usuario", "cargarUsuario", "id", "", "clearOperationResult", "crearUsuario", "nombre", "", "email", "telefono", "password", "rol", "direccion", "desactivarUsuario", "app_debug"})
public final class UsuarioViewModel extends androidx.lifecycle.AndroidViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.example.tomenaguita.data.repository.UsuarioRepository roomRepo = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.tomenaguita.data.repository.FirestoreUsuarioRepository firestoreRepo = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<java.util.List<com.example.tomenaguita.data.database.entity.Usuario>> todosLosUsuarios = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<com.example.tomenaguita.data.database.entity.Usuario> _usuarioActual = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<com.example.tomenaguita.data.database.entity.Usuario> usuarioActual = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<kotlin.Result<kotlin.Unit>> _operationResult = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<kotlin.Result<kotlin.Unit>> operationResult = null;
    
    public UsuarioViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application app) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<com.example.tomenaguita.data.database.entity.Usuario>> getTodosLosUsuarios() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.example.tomenaguita.data.database.entity.Usuario> getUsuarioActual() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<kotlin.Result<kotlin.Unit>> getOperationResult() {
        return null;
    }
    
    public final void clearOperationResult() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job cargarUsuario(long id) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job actualizarPerfil(@org.jetbrains.annotations.NotNull()
    com.example.tomenaguita.data.database.entity.Usuario usuario) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job crearUsuario(@org.jetbrains.annotations.NotNull()
    java.lang.String nombre, @org.jetbrains.annotations.NotNull()
    java.lang.String email, @org.jetbrains.annotations.NotNull()
    java.lang.String telefono, @org.jetbrains.annotations.NotNull()
    java.lang.String password, @org.jetbrains.annotations.NotNull()
    java.lang.String rol, @org.jetbrains.annotations.NotNull()
    java.lang.String direccion) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job desactivarUsuario(long id) {
        return null;
    }
}