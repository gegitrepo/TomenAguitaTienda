package com.example.tomenaguita.ui.comprador.pago;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010%\u001a\u00020&2\u0006\u0010\'\u001a\u00020(H\u0002J\b\u0010)\u001a\u00020&H\u0002J$\u0010*\u001a\u00020+2\u0006\u0010,\u001a\u00020-2\b\u0010.\u001a\u0004\u0018\u00010/2\b\u00100\u001a\u0004\u0018\u000101H\u0016J\b\u00102\u001a\u00020&H\u0016J\b\u00103\u001a\u00020&H\u0002J\u001a\u00104\u001a\u00020&2\u0006\u00105\u001a\u00020+2\b\u00100\u001a\u0004\u0018\u000101H\u0016R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0005\u001a\u00020\u00068BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\t\u0010\n\u001a\u0004\b\u0007\u0010\bR\u0014\u0010\u000b\u001a\u00020\u00048BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\f\u0010\rR\u001b\u0010\u000e\u001a\u00020\u000f8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0012\u0010\u0013\u001a\u0004\b\u0010\u0010\u0011R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0015X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0016\u001a\u00020\u00178BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u001a\u0010\u0013\u001a\u0004\b\u0018\u0010\u0019R\u001b\u0010\u001b\u001a\u00020\u001c8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u001f\u0010\u0013\u001a\u0004\b\u001d\u0010\u001eR\u001b\u0010 \u001a\u00020!8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b$\u0010\u0013\u001a\u0004\b\"\u0010#\u00a8\u00066"}, d2 = {"Lcom/example/tomenaguita/ui/comprador/pago/PasarelaPagoFragment;", "Landroidx/fragment/app/Fragment;", "()V", "_binding", "Lcom/example/tomenaguita/databinding/FragmentPasarelaPagoBinding;", "args", "Lcom/example/tomenaguita/ui/comprador/pago/PasarelaPagoFragmentArgs;", "getArgs", "()Lcom/example/tomenaguita/ui/comprador/pago/PasarelaPagoFragmentArgs;", "args$delegate", "Landroidx/navigation/NavArgsLazy;", "binding", "getBinding", "()Lcom/example/tomenaguita/databinding/FragmentPasarelaPagoBinding;", "carritoViewModel", "Lcom/example/tomenaguita/viewmodel/CarritoViewModel;", "getCarritoViewModel", "()Lcom/example/tomenaguita/viewmodel/CarritoViewModel;", "carritoViewModel$delegate", "Lkotlin/Lazy;", "clientSecret", "", "paymentSheet", "Lcom/stripe/android/paymentsheet/PaymentSheet;", "getPaymentSheet", "()Lcom/stripe/android/paymentsheet/PaymentSheet;", "paymentSheet$delegate", "pedidoViewModel", "Lcom/example/tomenaguita/viewmodel/PedidoViewModel;", "getPedidoViewModel", "()Lcom/example/tomenaguita/viewmodel/PedidoViewModel;", "pedidoViewModel$delegate", "productoViewModel", "Lcom/example/tomenaguita/viewmodel/ProductoViewModel;", "getProductoViewModel", "()Lcom/example/tomenaguita/viewmodel/ProductoViewModel;", "productoViewModel$delegate", "handlePaymentResult", "", "result", "Lcom/stripe/android/paymentsheet/PaymentSheetResult;", "iniciarPago", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onDestroyView", "onPagoCompletado", "onViewCreated", "view", "app_debug"})
public final class PasarelaPagoFragment extends androidx.fragment.app.Fragment {
    @org.jetbrains.annotations.Nullable()
    private com.example.tomenaguita.databinding.FragmentPasarelaPagoBinding _binding;
    @org.jetbrains.annotations.NotNull()
    private final androidx.navigation.NavArgsLazy args$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy pedidoViewModel$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy carritoViewModel$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy productoViewModel$delegate = null;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String clientSecret;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy paymentSheet$delegate = null;
    
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
    
    private final com.example.tomenaguita.viewmodel.ProductoViewModel getProductoViewModel() {
        return null;
    }
    
    private final com.stripe.android.paymentsheet.PaymentSheet getPaymentSheet() {
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
    
    private final void iniciarPago() {
    }
    
    private final void handlePaymentResult(com.stripe.android.paymentsheet.PaymentSheetResult result) {
    }
    
    private final void onPagoCompletado() {
    }
    
    @java.lang.Override()
    public void onDestroyView() {
    }
}