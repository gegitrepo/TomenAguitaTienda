package com.example.tomenaguita.viewmodel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\b2\u0006\u0010\u0012\u001a\u00020\u0013J,\u0010\u0014\u001a\u00020\u00102\u0006\u0010\u0015\u001a\u00020\b2\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00180\u00172\u0006\u0010\u0019\u001a\u00020\u00132\u0006\u0010\u001a\u001a\u00020\u0013J\b\u0010\u001b\u001a\u00020\u0013H\u0002J\u0012\u0010\u001c\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001d0\u00170\nJ\u001a\u0010\u001e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001d0\u00170\n2\u0006\u0010\u0015\u001a\u00020\bR\u001a\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001f"}, d2 = {"Lcom/example/tomenaguita/viewmodel/PedidoViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "app", "Landroid/app/Application;", "(Landroid/app/Application;)V", "_operationResult", "Landroidx/lifecycle/MutableLiveData;", "Lkotlin/Result;", "", "operationResult", "Landroidx/lifecycle/LiveData;", "getOperationResult", "()Landroidx/lifecycle/LiveData;", "repo", "Lcom/example/tomenaguita/data/repository/PedidoRepository;", "avanzarEstado", "Lkotlinx/coroutines/Job;", "pedidoId", "estadoActual", "", "crearPedido", "usuarioId", "items", "", "Lcom/example/tomenaguita/data/database/entity/DetallePedido;", "direccion", "metodoPago", "generarNumeroPedido", "getAllPedidos", "Lcom/example/tomenaguita/data/database/entity/Pedido;", "getPedidosByUsuario", "app_debug"})
public final class PedidoViewModel extends androidx.lifecycle.AndroidViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.example.tomenaguita.data.repository.PedidoRepository repo = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<kotlin.Result<java.lang.Long>> _operationResult = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<kotlin.Result<java.lang.Long>> operationResult = null;
    
    public PedidoViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application app) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<kotlin.Result<java.lang.Long>> getOperationResult() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<com.example.tomenaguita.data.database.entity.Pedido>> getPedidosByUsuario(long usuarioId) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<com.example.tomenaguita.data.database.entity.Pedido>> getAllPedidos() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job crearPedido(long usuarioId, @org.jetbrains.annotations.NotNull()
    java.util.List<com.example.tomenaguita.data.database.entity.DetallePedido> items, @org.jetbrains.annotations.NotNull()
    java.lang.String direccion, @org.jetbrains.annotations.NotNull()
    java.lang.String metodoPago) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job avanzarEstado(long pedidoId, @org.jetbrains.annotations.NotNull()
    java.lang.String estadoActual) {
        return null;
    }
    
    private final java.lang.String generarNumeroPedido() {
        return null;
    }
}