package com.example.tomenaguita.ui.admin.usuarios;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000d\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\u0018\u0000 12\u00020\u0001:\u00011B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0018\u001a\u00020\u0019H\u0002J\b\u0010\u001a\u001a\u00020\u0019H\u0002J\u0018\u0010\u001b\u001a\u00020\u00192\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001dH\u0002J$\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\"2\b\u0010#\u001a\u0004\u0018\u00010$2\b\u0010%\u001a\u0004\u0018\u00010&H\u0016J\b\u0010\'\u001a\u00020\u0019H\u0016J\b\u0010(\u001a\u00020\u0019H\u0016J\b\u0010)\u001a\u00020\u0019H\u0016J\b\u0010*\u001a\u00020\u0019H\u0016J\u0010\u0010+\u001a\u00020\u00192\u0006\u0010,\u001a\u00020&H\u0016J\u001a\u0010-\u001a\u00020\u00192\u0006\u0010.\u001a\u00020 2\b\u0010%\u001a\u0004\u0018\u00010&H\u0016J\u0012\u0010/\u001a\u00020\u00192\b\u0010%\u001a\u0004\u0018\u00010&H\u0002J\b\u00100\u001a\u00020\u0019H\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\u00020\u00048BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007R\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001c\u0010\f\u001a\u0010\u0012\f\u0012\n \u000f*\u0004\u0018\u00010\u000e0\u000e0\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0010\u001a\u0004\u0018\u00010\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0012\u001a\u00020\u00138BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0016\u0010\u0017\u001a\u0004\b\u0014\u0010\u0015\u00a8\u00062"}, d2 = {"Lcom/example/tomenaguita/ui/admin/usuarios/CrearUsuarioFragment;", "Landroidx/fragment/app/Fragment;", "()V", "_binding", "Lcom/example/tomenaguita/databinding/FragmentCrearUsuarioBinding;", "binding", "getBinding", "()Lcom/example/tomenaguita/databinding/FragmentCrearUsuarioBinding;", "geocodingJob", "Lkotlinx/coroutines/Job;", "googleMap", "Lcom/google/android/gms/maps/GoogleMap;", "locationPermissionLauncher", "Landroidx/activity/result/ActivityResultLauncher;", "", "kotlin.jvm.PlatformType", "selectedMarker", "Lcom/google/android/gms/maps/model/Marker;", "viewModel", "Lcom/example/tomenaguita/viewmodel/UsuarioViewModel;", "getViewModel", "()Lcom/example/tomenaguita/viewmodel/UsuarioViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "crearUsuario", "", "detectarUbicacion", "geocodearDireccion", "lat", "", "lng", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onDestroyView", "onLowMemory", "onPause", "onResume", "onSaveInstanceState", "outState", "onViewCreated", "view", "setupMapView", "solicitarUbicacion", "Companion", "app_debug"})
public final class CrearUsuarioFragment extends androidx.fragment.app.Fragment {
    @org.jetbrains.annotations.Nullable()
    private com.example.tomenaguita.databinding.FragmentCrearUsuarioBinding _binding;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    @org.jetbrains.annotations.Nullable()
    private com.google.android.gms.maps.GoogleMap googleMap;
    @org.jetbrains.annotations.Nullable()
    private com.google.android.gms.maps.model.Marker selectedMarker;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job geocodingJob;
    @org.jetbrains.annotations.NotNull()
    private final androidx.activity.result.ActivityResultLauncher<java.lang.String> locationPermissionLauncher = null;
    @org.jetbrains.annotations.NotNull()
    private static final com.google.android.gms.maps.model.LatLng BOGOTA_DEFAULT = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.example.tomenaguita.ui.admin.usuarios.CrearUsuarioFragment.Companion Companion = null;
    
    public CrearUsuarioFragment() {
        super();
    }
    
    private final com.example.tomenaguita.databinding.FragmentCrearUsuarioBinding getBinding() {
        return null;
    }
    
    private final com.example.tomenaguita.viewmodel.UsuarioViewModel getViewModel() {
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
    
    private final void setupMapView(android.os.Bundle savedInstanceState) {
    }
    
    private final void solicitarUbicacion() {
    }
    
    private final void detectarUbicacion() {
    }
    
    private final void geocodearDireccion(double lat, double lng) {
    }
    
    private final void crearUsuario() {
    }
    
    @java.lang.Override()
    public void onSaveInstanceState(@org.jetbrains.annotations.NotNull()
    android.os.Bundle outState) {
    }
    
    @java.lang.Override()
    public void onResume() {
    }
    
    @java.lang.Override()
    public void onPause() {
    }
    
    @java.lang.Override()
    public void onLowMemory() {
    }
    
    @java.lang.Override()
    public void onDestroyView() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/example/tomenaguita/ui/admin/usuarios/CrearUsuarioFragment$Companion;", "", "()V", "BOGOTA_DEFAULT", "Lcom/google/android/gms/maps/model/LatLng;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}