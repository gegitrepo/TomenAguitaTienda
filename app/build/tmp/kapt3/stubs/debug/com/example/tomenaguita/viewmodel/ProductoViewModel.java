package com.example.tomenaguita.viewmodel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u0018\u001a\u00020\bJ\u000e\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001cJ\u001a\u0010\u001d\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u00140\u000e2\u0006\u0010\u001e\u001a\u00020\u001cJ\"\u0010\u001f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u00140\u00062\u0006\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020\u001cJ\"\u0010#\u001a\u00020\u001a2\f\u0010$\u001a\b\u0012\u0004\u0012\u00020%0\u00142\f\u0010&\u001a\b\u0012\u0004\u0012\u00020\n0\u0014J\u000e\u0010\'\u001a\u00020\u001a2\u0006\u0010(\u001a\u00020\nJ\u000e\u0010)\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001cR\u001c\u0010\u0005\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\b\u0018\u00010\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\t\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001f\u0010\r\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\b\u0018\u00010\u00070\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0019\u0010\u0011\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0010R\u001d\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u00140\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0010R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006*"}, d2 = {"Lcom/example/tomenaguita/viewmodel/ProductoViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "app", "Landroid/app/Application;", "(Landroid/app/Application;)V", "_operationResult", "Landroidx/lifecycle/MutableLiveData;", "Lkotlin/Result;", "", "_productoSeleccionado", "Lcom/example/tomenaguita/data/database/entity/Producto;", "firestoreRepo", "Lcom/example/tomenaguita/data/repository/FirestoreProductoRepository;", "operationResult", "Landroidx/lifecycle/LiveData;", "getOperationResult", "()Landroidx/lifecycle/LiveData;", "productoSeleccionado", "getProductoSeleccionado", "productosDisponibles", "", "getProductosDisponibles", "roomRepo", "Lcom/example/tomenaguita/data/repository/ProductoRepository;", "clearOperationResult", "deleteProducto", "Lkotlinx/coroutines/Job;", "id", "", "getProductosByVendedor", "vendedorId", "getProductosByVendedorFirestore", "vendedorUid", "", "vendedorRoomId", "reducirStock", "cartItems", "Lcom/example/tomenaguita/data/database/entity/CarritoItem;", "productos", "saveProducto", "producto", "selectProducto", "app_debug"})
public final class ProductoViewModel extends androidx.lifecycle.AndroidViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.example.tomenaguita.data.repository.ProductoRepository roomRepo = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.tomenaguita.data.repository.FirestoreProductoRepository firestoreRepo = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<java.util.List<com.example.tomenaguita.data.database.entity.Producto>> productosDisponibles = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<com.example.tomenaguita.data.database.entity.Producto> _productoSeleccionado = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<com.example.tomenaguita.data.database.entity.Producto> productoSeleccionado = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<kotlin.Result<kotlin.Unit>> _operationResult = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<kotlin.Result<kotlin.Unit>> operationResult = null;
    
    public ProductoViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application app) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<com.example.tomenaguita.data.database.entity.Producto>> getProductosDisponibles() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.example.tomenaguita.data.database.entity.Producto> getProductoSeleccionado() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<kotlin.Result<kotlin.Unit>> getOperationResult() {
        return null;
    }
    
    public final void clearOperationResult() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<com.example.tomenaguita.data.database.entity.Producto>> getProductosByVendedor(long vendedorId) {
        return null;
    }
    
    /**
     * Devuelve en tiempo real los productos del vendedor consultando Firestore por su UID.
     * Para cada producto, busca el registro en Room por firestoreDocId para obtener el Room ID
     * correcto (necesario para la navegación de edición). Si no está en Room aún, lo inserta
     * con el vendedorRoomId indicado para que el listado y la edición funcionen correctamente.
     * Esto resuelve el caso de Room vacío (reinstalación / clear data).
     */
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MutableLiveData<java.util.List<com.example.tomenaguita.data.database.entity.Producto>> getProductosByVendedorFirestore(@org.jetbrains.annotations.NotNull()
    java.lang.String vendedorUid, long vendedorRoomId) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job selectProducto(long id) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job saveProducto(@org.jetbrains.annotations.NotNull()
    com.example.tomenaguita.data.database.entity.Producto producto) {
        return null;
    }
    
    /**
     * Resta del inventario las cantidades compradas en una orden exitosa.
     * Actualiza Room y Firestore. Corre en viewModelScope — sobrevive la navegación.
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job reducirStock(@org.jetbrains.annotations.NotNull()
    java.util.List<com.example.tomenaguita.data.database.entity.CarritoItem> cartItems, @org.jetbrains.annotations.NotNull()
    java.util.List<com.example.tomenaguita.data.database.entity.Producto> productos) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job deleteProducto(long id) {
        return null;
    }
}