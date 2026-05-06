package com.example.tomenaguita.viewmodel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014J\u000e\u0010\u0015\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012J\u0014\u0010\u0016\u001a\u00020\u00172\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00120\u0019J\u000e\u0010\u001a\u001a\u00020\u00102\u0006\u0010\u001b\u001a\u00020\u001cJ\u001a\u0010\u001d\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\u00190\f2\u0006\u0010\u001e\u001a\u00020\u001cJ\u0014\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u00140\f2\u0006\u0010\u001e\u001a\u00020\u001cJ\u000e\u0010 \u001a\u00020\u00102\u0006\u0010\u001e\u001a\u00020\u001cR\u001c\u0010\u0005\u001a\u0010\u0012\f\u0012\n \b*\u0004\u0018\u00010\u00070\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00070\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e\u00a8\u0006!"}, d2 = {"Lcom/example/tomenaguita/viewmodel/CarritoViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "app", "Landroid/app/Application;", "(Landroid/app/Application;)V", "_total", "Landroidx/lifecycle/MutableLiveData;", "", "kotlin.jvm.PlatformType", "repo", "Lcom/example/tomenaguita/data/repository/CarritoRepository;", "total", "Landroidx/lifecycle/LiveData;", "getTotal", "()Landroidx/lifecycle/LiveData;", "actualizarCantidad", "Lkotlinx/coroutines/Job;", "item", "Lcom/example/tomenaguita/data/database/entity/CarritoItem;", "nuevaCantidad", "", "agregarItem", "calcularTotal", "", "items", "", "eliminarItem", "id", "", "getCarrito", "usuarioId", "getCount", "vaciarCarrito", "app_debug"})
public final class CarritoViewModel extends androidx.lifecycle.AndroidViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.example.tomenaguita.data.repository.CarritoRepository repo = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.lang.Double> _total = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<java.lang.Double> total = null;
    
    public CarritoViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application app) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.lang.Double> getTotal() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<com.example.tomenaguita.data.database.entity.CarritoItem>> getCarrito(long usuarioId) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.lang.Integer> getCount(long usuarioId) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job agregarItem(@org.jetbrains.annotations.NotNull()
    com.example.tomenaguita.data.database.entity.CarritoItem item) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job actualizarCantidad(@org.jetbrains.annotations.NotNull()
    com.example.tomenaguita.data.database.entity.CarritoItem item, int nuevaCantidad) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job eliminarItem(long id) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job vaciarCarrito(long usuarioId) {
        return null;
    }
    
    public final void calcularTotal(@org.jetbrains.annotations.NotNull()
    java.util.List<com.example.tomenaguita.data.database.entity.CarritoItem> items) {
    }
}