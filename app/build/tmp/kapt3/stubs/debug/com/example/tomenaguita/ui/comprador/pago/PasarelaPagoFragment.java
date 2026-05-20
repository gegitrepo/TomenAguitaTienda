package com.example.tomenaguita.ui.comprador.pago;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0019\u001a\u00020\u001aH\u0002J\b\u0010\u001b\u001a\u00020\u001aH\u0002J\u0010\u0010\u001c\u001a\u00020\u001a2\u0006\u0010\u001d\u001a\u00020\u001eH\u0002J\b\u0010\u001f\u001a\u00020\u001aH\u0002J\u0010\u0010 \u001a\u00020\u001a2\u0006\u0010!\u001a\u00020\"H\u0002J$\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020&2\b\u0010\'\u001a\u0004\u0018\u00010(2\b\u0010)\u001a\u0004\u0018\u00010*H\u0016J\b\u0010+\u001a\u00020\u001aH\u0016J\u001a\u0010,\u001a\u00020\u001a2\u0006\u0010-\u001a\u00020$2\b\u0010)\u001a\u0004\u0018\u00010*H\u0017J\b\u0010.\u001a\u00020\u001aH\u0003R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0005\u001a\u00020\u00068BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\t\u0010\n\u001a\u0004\b\u0007\u0010\bR\u0014\u0010\u000b\u001a\u00020\u00048BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\f\u0010\rR\u001b\u0010\u000e\u001a\u00020\u000f8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0012\u0010\u0013\u001a\u0004\b\u0010\u0010\u0011R\u001b\u0010\u0014\u001a\u00020\u00158BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0018\u0010\u0013\u001a\u0004\b\u0016\u0010\u0017\u00a8\u0006/"}, d2 = {"Lcom/example/tomenaguita/ui/comprador/pago/PasarelaPagoFragment;", "Landroidx/fragment/app/Fragment;", "()V", "_binding", "Lcom/example/tomenaguita/databinding/FragmentPasarelaPagoBinding;", "args", "Lcom/example/tomenaguita/ui/comprador/pago/PasarelaPagoFragmentArgs;", "getArgs", "()Lcom/example/tomenaguita/ui/comprador/pago/PasarelaPagoFragmentArgs;", "args$delegate", "Landroidx/navigation/NavArgsLazy;", "binding", "getBinding", "()Lcom/example/tomenaguita/databinding/FragmentPasarelaPagoBinding;", "carritoViewModel", "Lcom/example/tomenaguita/viewmodel/CarritoViewModel;", "getCarritoViewModel", "()Lcom/example/tomenaguita/viewmodel/CarritoViewModel;", "carritoViewModel$delegate", "Lkotlin/Lazy;", "pedidoViewModel", "Lcom/example/tomenaguita/viewmodel/PedidoViewModel;", "getPedidoViewModel", "()Lcom/example/tomenaguita/viewmodel/PedidoViewModel;", "pedidoViewModel$delegate", "handleFailure", "", "handlePending", "handleSuccess", "url", "", "iniciarPagoMP", "ocultarBottomNav", "ocultar", "", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onDestroyView", "onViewCreated", "view", "setupWebView", "app_debug"})
public final class PasarelaPagoFragment extends androidx.fragment.app.Fragment {
    @org.jetbrains.annotations.Nullable()
    private com.example.tomenaguita.databinding.FragmentPasarelaPagoBinding _binding;
    @org.jetbrains.annotations.NotNull()
    private final androidx.navigation.NavArgsLazy args$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy pedidoViewModel$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy carritoViewModel$delegate = null;
    
    public PasarelaPagoFragment() {
        super();
    }
    
    private final com.example.tomenaguita.databinding.FragmentPasarelaPagoBinding getBinding() {
        return null;
    }
    
    private final com.example.tomenaguita.ui.comprador.pago.PasarelaPagoFragmentArgs getArgs() {
        return null;
    }
    
    private final com.example.tomenaguita.viewmodel.PedidoViewModel getPedidoViewModel() {
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
    @android.annotation.SuppressLint(value = {"SetJavaScriptEnabled"})
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @android.annotation.SuppressLint(value = {"SetJavaScriptEnabled"})
    private final void setupWebView() {
    }
    
    private final void iniciarPagoMP() {
    }
    
    private final void handleSuccess(java.lang.String url) {
    }
    
    private final void handleFailure() {
    }
    
    private final void handlePending() {
    }
    
    /**
     * Oculta o muestra el BottomNav de CompradorMainActivity durante el checkout.
     */
    private final void ocultarBottomNav(boolean ocultar) {
    }
    
    @java.lang.Override()
    public void onDestroyView() {
    }
}