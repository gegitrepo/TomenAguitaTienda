package com.example.tomenaguita.ui.vendedor.productos;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u0000 12\u00020\u0001:\u00011B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010 \u001a\u00020!H\u0002J\b\u0010\"\u001a\u00020!H\u0002J$\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020&2\b\u0010\'\u001a\u0004\u0018\u00010(2\b\u0010)\u001a\u0004\u0018\u00010*H\u0016J\b\u0010+\u001a\u00020!H\u0016J\u0010\u0010,\u001a\u00020!2\u0006\u0010-\u001a\u00020*H\u0016J\u001a\u0010.\u001a\u00020!2\u0006\u0010/\u001a\u00020$2\b\u0010)\u001a\u0004\u0018\u00010*H\u0016J\b\u00100\u001a\u00020!H\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0005\u001a\u00020\u00068BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\t\u0010\n\u001a\u0004\b\u0007\u0010\bR\u0014\u0010\u000b\u001a\u00020\u00048BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\f\u0010\rR\u001c\u0010\u000e\u001a\u0010\u0012\f\u0012\n \u0011*\u0004\u0018\u00010\u00100\u00100\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u0012\u001a\u0010\u0012\f\u0012\n \u0011*\u0004\u0018\u00010\u00130\u00130\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u0014\u001a\u0010\u0012\f\u0012\n \u0011*\u0004\u0018\u00010\u00130\u00130\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0015\u001a\u00020\u00168BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0015\u0010\u0017R\u0010\u0010\u0018\u001a\u0004\u0018\u00010\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0019\u001a\u0004\u0018\u00010\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u001a\u001a\u00020\u001b8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u001e\u0010\u001f\u001a\u0004\b\u001c\u0010\u001d\u00a8\u00062"}, d2 = {"Lcom/example/tomenaguita/ui/vendedor/productos/CrearEditarProductoFragment;", "Landroidx/fragment/app/Fragment;", "()V", "_binding", "Lcom/example/tomenaguita/databinding/FragmentCrearEditarProductoBinding;", "args", "Lcom/example/tomenaguita/ui/vendedor/productos/CrearEditarProductoFragmentArgs;", "getArgs", "()Lcom/example/tomenaguita/ui/vendedor/productos/CrearEditarProductoFragmentArgs;", "args$delegate", "Landroidx/navigation/NavArgsLazy;", "binding", "getBinding", "()Lcom/example/tomenaguita/databinding/FragmentCrearEditarProductoBinding;", "cameraLauncher", "Landroidx/activity/result/ActivityResultLauncher;", "Landroid/net/Uri;", "kotlin.jvm.PlatformType", "cameraPermissionLauncher", "", "galleryLauncher", "isEditing", "", "()Z", "pendingCameraUri", "selectedImageUri", "viewModel", "Lcom/example/tomenaguita/viewmodel/ProductoViewModel;", "getViewModel", "()Lcom/example/tomenaguita/viewmodel/ProductoViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "abrirCamara", "", "guardar", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onDestroyView", "onSaveInstanceState", "outState", "onViewCreated", "view", "solicitarCamara", "Companion", "app_debug"})
public final class CrearEditarProductoFragment extends androidx.fragment.app.Fragment {
    @org.jetbrains.annotations.Nullable()
    private com.example.tomenaguita.databinding.FragmentCrearEditarProductoBinding _binding;
    @org.jetbrains.annotations.NotNull()
    private final androidx.navigation.NavArgsLazy args$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    @org.jetbrains.annotations.Nullable()
    private android.net.Uri selectedImageUri;
    @org.jetbrains.annotations.Nullable()
    private android.net.Uri pendingCameraUri;
    @org.jetbrains.annotations.NotNull()
    private final androidx.activity.result.ActivityResultLauncher<java.lang.String> cameraPermissionLauncher = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.activity.result.ActivityResultLauncher<java.lang.String> galleryLauncher = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.activity.result.ActivityResultLauncher<android.net.Uri> cameraLauncher = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_CAMERA_URI = "pending_camera_uri";
    @org.jetbrains.annotations.NotNull()
    public static final com.example.tomenaguita.ui.vendedor.productos.CrearEditarProductoFragment.Companion Companion = null;
    
    public CrearEditarProductoFragment() {
        super();
    }
    
    private final com.example.tomenaguita.databinding.FragmentCrearEditarProductoBinding getBinding() {
        return null;
    }
    
    private final com.example.tomenaguita.ui.vendedor.productos.CrearEditarProductoFragmentArgs getArgs() {
        return null;
    }
    
    private final boolean isEditing() {
        return false;
    }
    
    private final com.example.tomenaguita.viewmodel.ProductoViewModel getViewModel() {
        return null;
    }
    
    @java.lang.Override()
    public void onSaveInstanceState(@org.jetbrains.annotations.NotNull()
    android.os.Bundle outState) {
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
    
    private final void solicitarCamara() {
    }
    
    private final void abrirCamara() {
    }
    
    private final void guardar() {
    }
    
    @java.lang.Override()
    public void onDestroyView() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/example/tomenaguita/ui/vendedor/productos/CrearEditarProductoFragment$Companion;", "", "()V", "KEY_CAMERA_URI", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}