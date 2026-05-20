package com.example.tomenaguita.ui.comprador.home;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001eH\u0002J$\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\"2\b\u0010#\u001a\u0004\u0018\u00010$2\b\u0010%\u001a\u0004\u0018\u00010&H\u0016J\b\u0010\'\u001a\u00020\u001cH\u0016J\u001a\u0010(\u001a\u00020\u001c2\u0006\u0010)\u001a\u00020 2\b\u0010%\u001a\u0004\u0018\u00010&H\u0016R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\u00020\u00048BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\fR\u001b\u0010\r\u001a\u00020\u000e8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0011\u0010\u0012\u001a\u0004\b\u000f\u0010\u0010R\u0014\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0016\u001a\u00020\u00178BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u001a\u0010\u0012\u001a\u0004\b\u0018\u0010\u0019\u00a8\u0006*"}, d2 = {"Lcom/example/tomenaguita/ui/comprador/home/HomeFragment;", "Landroidx/fragment/app/Fragment;", "()V", "_binding", "Lcom/example/tomenaguita/databinding/FragmentHomeBinding;", "adapter", "Lcom/example/tomenaguita/ui/adapter/ProductoAdapter;", "allProductos", "", "Lcom/example/tomenaguita/data/database/entity/Producto;", "binding", "getBinding", "()Lcom/example/tomenaguita/databinding/FragmentHomeBinding;", "carritoViewModel", "Lcom/example/tomenaguita/viewmodel/CarritoViewModel;", "getCarritoViewModel", "()Lcom/example/tomenaguita/viewmodel/CarritoViewModel;", "carritoViewModel$delegate", "Lkotlin/Lazy;", "currentFeatured", "destacadosSeleccionados", "", "productoViewModel", "Lcom/example/tomenaguita/viewmodel/ProductoViewModel;", "getProductoViewModel", "()Lcom/example/tomenaguita/viewmodel/ProductoViewModel;", "productoViewModel$delegate", "applySearch", "", "query", "", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onDestroyView", "onViewCreated", "view", "app_debug"})
public final class HomeFragment extends androidx.fragment.app.Fragment {
    @org.jetbrains.annotations.Nullable()
    private com.example.tomenaguita.databinding.FragmentHomeBinding _binding;
    private com.example.tomenaguita.ui.adapter.ProductoAdapter adapter;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy productoViewModel$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy carritoViewModel$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private java.util.List<com.example.tomenaguita.data.database.entity.Producto> allProductos;
    @org.jetbrains.annotations.NotNull()
    private java.util.List<com.example.tomenaguita.data.database.entity.Producto> currentFeatured;
    private boolean destacadosSeleccionados = false;
    
    public HomeFragment() {
        super();
    }
    
    private final com.example.tomenaguita.databinding.FragmentHomeBinding getBinding() {
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
    
    private final void applySearch(java.lang.String query) {
    }
    
    @java.lang.Override()
    public void onDestroyView() {
    }
}