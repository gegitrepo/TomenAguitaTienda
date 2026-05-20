package com.example.tomenaguita.viewmodel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\r\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020 J\u0016\u0010\"\u001a\u00020\u001e2\u0006\u0010#\u001a\u00020\u000b2\u0006\u0010$\u001a\u00020 J\u000e\u0010%\u001a\u00020\u001e2\u0006\u0010#\u001a\u00020\u000bJ\u000e\u0010&\u001a\u00020\u001e2\u0006\u0010#\u001a\u00020\u000bJ\u0006\u0010\'\u001a\u00020(J,\u0010)\u001a\u00020\u001e2\u0006\u0010*\u001a\u00020\u000b2\f\u0010+\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\u0006\u0010,\u001a\u00020 2\u0006\u0010-\u001a\u00020 J\b\u0010.\u001a\u00020 H\u0002J\u0012\u0010/\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\u00070\u0010J\u001a\u00100\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\u00070\u00102\u0006\u0010*\u001a\u00020\u000bJ\u001a\u00101\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\u00070\u00102\u0006\u00102\u001a\u00020\u000bJ\u000e\u00103\u001a\u00020\u001e2\u0006\u00104\u001a\u00020\u000bR\u001a\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\n0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\f\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\r0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u000e\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\r0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u000f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0015\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\n0\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0012R\u0019\u0010\u0017\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\r0\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0012R\u000e\u0010\u0019\u001a\u00020\u001aX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\u001b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\r0\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0012\u00a8\u00065"}, d2 = {"Lcom/example/tomenaguita/viewmodel/PedidoViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "app", "Landroid/app/Application;", "(Landroid/app/Application;)V", "_detallesPedido", "Landroidx/lifecycle/MutableLiveData;", "", "Lcom/example/tomenaguita/data/database/entity/DetallePedido;", "_operationResult", "Lkotlin/Result;", "", "_pedidoActual", "Lcom/example/tomenaguita/data/database/entity/Pedido;", "_ultimoPedidoCreado", "detallesPedido", "Landroidx/lifecycle/LiveData;", "getDetallesPedido", "()Landroidx/lifecycle/LiveData;", "firestoreRepo", "Lcom/example/tomenaguita/data/repository/FirestorePedidoRepository;", "operationResult", "getOperationResult", "pedidoActual", "getPedidoActual", "roomRepo", "Lcom/example/tomenaguita/data/repository/PedidoRepository;", "ultimoPedidoCreado", "getUltimoPedidoCreado", "actualizarEstadoByOrderNumber", "Lkotlinx/coroutines/Job;", "orderNumber", "", "nuevoEstado", "avanzarEstado", "pedidoId", "estadoActual", "cancelarPedido", "cargarDetalles", "clearUltimoPedido", "", "crearPedido", "usuarioId", "items", "direccion", "metodoPago", "generarNumeroPedido", "getAllPedidos", "getPedidosByUsuario", "getPedidosByVendedor", "vendedorId", "selectPedido", "id", "app_debug"})
public final class PedidoViewModel extends androidx.lifecycle.AndroidViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.example.tomenaguita.data.repository.PedidoRepository roomRepo = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.tomenaguita.data.repository.FirestorePedidoRepository firestoreRepo = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<kotlin.Result<java.lang.Long>> _operationResult = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<kotlin.Result<java.lang.Long>> operationResult = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<com.example.tomenaguita.data.database.entity.Pedido> _pedidoActual = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<com.example.tomenaguita.data.database.entity.Pedido> pedidoActual = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<com.example.tomenaguita.data.database.entity.Pedido> _ultimoPedidoCreado = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<com.example.tomenaguita.data.database.entity.Pedido> ultimoPedidoCreado = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.util.List<com.example.tomenaguita.data.database.entity.DetallePedido>> _detallesPedido = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<java.util.List<com.example.tomenaguita.data.database.entity.DetallePedido>> detallesPedido = null;
    
    public PedidoViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application app) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<kotlin.Result<java.lang.Long>> getOperationResult() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.example.tomenaguita.data.database.entity.Pedido> getPedidoActual() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.example.tomenaguita.data.database.entity.Pedido> getUltimoPedidoCreado() {
        return null;
    }
    
    public final void clearUltimoPedido() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<com.example.tomenaguita.data.database.entity.DetallePedido>> getDetallesPedido() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job cargarDetalles(long pedidoId) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job cancelarPedido(long pedidoId) {
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
    public final androidx.lifecycle.LiveData<java.util.List<com.example.tomenaguita.data.database.entity.Pedido>> getPedidosByVendedor(long vendedorId) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job selectPedido(long id) {
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
    
    /**
     * Actualiza el estado de un pedido directamente por su orderNumber (usado tras el pago).
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job actualizarEstadoByOrderNumber(@org.jetbrains.annotations.NotNull()
    java.lang.String orderNumber, @org.jetbrains.annotations.NotNull()
    java.lang.String nuevoEstado) {
        return null;
    }
    
    private final java.lang.String generarNumeroPedido() {
        return null;
    }
}