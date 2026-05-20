package com.example.tomenaguita.ui.comprador.producto;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u0017H\u0002J$\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020#2\b\u0010$\u001a\u0004\u0018\u00010%2\b\u0010&\u001a\u0004\u0018\u00010\'H\u0016J\b\u0010(\u001a\u00020\u001eH\u0016J\u001a\u0010)\u001a\u00020\u001e2\u0006\u0010*\u001a\u00020!2\b\u0010&\u001a\u0004\u0018\u00010\'H\u0016J\b\u0010+\u001a\u00020\u001eH\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0005\u001a\u00020\u00068BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\t\u0010\n\u001a\u0004\b\u0007\u0010\bR\u0014\u0010\u000b\u001a\u00020\u00048BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\f\u0010\rR\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0010\u001a\u00020\u00118BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0014\u0010\u0015\u001a\u0004\b\u0012\u0010\u0013R\u0010\u0010\u0016\u001a\u0004\u0018\u00010\u0017X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0018\u001a\u00020\u00198BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u001c\u0010\u0015\u001a\u0004\b\u001a\u0010\u001b\u00a8\u0006,"}, d2 = {"Lcom/example/tomenaguita/ui/comprador/producto/ProductoDetalleFragment;", "Landroidx/fragment/app/Fragment;", "()V", "_binding", "Lcom/example/tomenaguita/databinding/FragmentProductoDetalleBinding;", "args", "Lcom/example/tomenaguita/ui/comprador/producto/ProductoDetalleFragmentArgs;", "getArgs", "()Lcom/example/tomenaguita/ui/comprador/producto/ProductoDetalleFragmentArgs;", "args$delegate", "Landroidx/navigation/NavArgsLazy;", "binding", "getBinding", "()Lcom/example/tomenaguita/databinding/FragmentProductoDetalleBinding;", "cantidad", "", "carritoViewModel", "Lcom/example/tomenaguita/viewmodel/CarritoViewModel;", "getCarritoViewModel", "()Lcom/example/tomenaguita/viewmodel/CarritoViewModel;", "carritoViewModel$delegate", "Lkotlin/Lazy;", "productoActual", "Lcom/example/tomenaguita/data/database/entity/Producto;", "productoViewModel", "Lcom/example/tomenaguita/viewmodel/ProductoViewModel;", "getProductoViewModel", "()Lcom/example/tomenaguita/viewmodel/ProductoViewModel;", "productoViewModel$delegate", "bindProducto", "", "producto", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onDestroyView", "onViewCreated", "view", "setupCantidad", "app_debug"})
public final class ProductoDetalleFragment extends androidx.fragment.app.Fragment {
    @org.jetbrains.annotations.Nullable()
    private com.example.tomenaguita.databinding.FragmentProductoDetalleBinding _binding;
    @org.jetbrains.annotations.NotNull()
    private final androidx.navigation.NavArgsLazy args$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy productoViewModel$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy carritoViewModel$delegate = null;
    private int cantidad = 1;
    @org.jetbrains.annotations.Nullable()
    private com.example.tomenaguita.data.database.entity.Producto productoActual;
    
    public ProductoDetalleFragment() {
        super();
    }
    
    private final com.example.tomenaguita.databinding.FragmentProductoDetalleBinding getBinding() {
        return null;
    }
    
    private final com.example.tomenaguita.ui.comprador.producto.ProductoDetalleFragmentArgs getArgs() {
        return null;
    }
    
    private final com.example.tomenaguita.viewmodel.ProductoViewModel getProductoViewModel() {
        return null;
    }
    
    private final com.example.tomenaguita.viewmodel.CarritoViewModel getCarritoViewModel() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable()
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void bindProducto(com.example.tomenaguita.data.database.entity.Producto producto) {
    }
    
    private final void setupCantidad() {
    }
    
    @java.lang.Override()
    public void onDestroyView() {
    }
}