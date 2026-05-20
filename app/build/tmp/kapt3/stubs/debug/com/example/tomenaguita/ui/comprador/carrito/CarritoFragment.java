package com.example.tomenaguita.ui.comprador.carrito;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010$\n\u0002\u0010\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J$\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001f2\b\u0010 \u001a\u0004\u0018\u00010!2\b\u0010\"\u001a\u0004\u0018\u00010#H\u0016J\b\u0010$\u001a\u00020%H\u0016J\u001a\u0010&\u001a\u00020%2\u0006\u0010\'\u001a\u00020\u001d2\b\u0010\"\u001a\u0004\u0018\u00010#H\u0016J\b\u0010(\u001a\u00020%H\u0002J\b\u0010)\u001a\u00020%H\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\u00020\u00048BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\b\u0010\tR\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\r\u001a\u00020\u000e8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0011\u0010\u0012\u001a\u0004\b\u000f\u0010\u0010R\u001a\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u0015\u0012\u0004\u0012\u00020\u00160\u0014X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0017\u001a\u00020\u00188BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u001b\u0010\u0012\u001a\u0004\b\u0019\u0010\u001a\u00a8\u0006*"}, d2 = {"Lcom/example/tomenaguita/ui/comprador/carrito/CarritoFragment;", "Landroidx/fragment/app/Fragment;", "()V", "_binding", "Lcom/example/tomenaguita/databinding/FragmentCarritoBinding;", "adapter", "Lcom/example/tomenaguita/ui/adapter/CarritoAdapter;", "binding", "getBinding", "()Lcom/example/tomenaguita/databinding/FragmentCarritoBinding;", "carritoItems", "", "Lcom/example/tomenaguita/data/database/entity/CarritoItem;", "carritoViewModel", "Lcom/example/tomenaguita/viewmodel/CarritoViewModel;", "getCarritoViewModel", "()Lcom/example/tomenaguita/viewmodel/CarritoViewModel;", "carritoViewModel$delegate", "Lkotlin/Lazy;", "productoMap", "", "", "Lcom/example/tomenaguita/data/database/entity/Producto;", "productoViewModel", "Lcom/example/tomenaguita/viewmodel/ProductoViewModel;", "getProductoViewModel", "()Lcom/example/tomenaguita/viewmodel/ProductoViewModel;", "productoViewModel$delegate", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onDestroyView", "", "onViewCreated", "view", "rebuildUI", "setupAdapter", "app_debug"})
public final class CarritoFragment extends androidx.fragment.app.Fragment {
    @org.jetbrains.annotations.Nullable()
    private com.example.tomenaguita.databinding.FragmentCarritoBinding _binding;
    private com.example.tomenaguita.ui.adapter.CarritoAdapter adapter;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy carritoViewModel$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy productoViewModel$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private java.util.Map<java.lang.Long, com.example.tomenaguita.data.database.entity.Producto> productoMap;
    @org.jetbrains.annotations.NotNull()
    private java.util.List<com.example.tomenaguita.data.database.entity.CarritoItem> carritoItems;
    
    public CarritoFragment() {
        super();
    }
    
    private final com.example.tomenaguita.databinding.FragmentCarritoBinding getBinding() {
        return null;
    }
    
    private final com.example.tomenaguita.viewmodel.CarritoViewModel getCarritoViewModel() {
        return null;
    }
    
    private final com.example.tomenaguita.viewmodel.ProductoViewModel getProductoViewModel() {
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
    
    private final void setupAdapter() {
    }
    
    private final void rebuildUI() {
    }
    
    @java.lang.Override()
    public void onDestroyView() {
    }
}