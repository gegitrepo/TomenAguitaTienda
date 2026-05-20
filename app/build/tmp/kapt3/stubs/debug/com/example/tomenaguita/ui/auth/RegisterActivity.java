package com.example.tomenaguita.ui.auth;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\n\u0018\u0000 %2\u00020\u0001:\u0001%B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0013\u001a\u00020\u0014H\u0002J\b\u0010\u0015\u001a\u00020\u0014H\u0002J\u0018\u0010\u0016\u001a\u00020\u00142\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u0018H\u0002J\u0012\u0010\u001a\u001a\u00020\u00142\b\u0010\u001b\u001a\u0004\u0018\u00010\u001cH\u0014J\b\u0010\u001d\u001a\u00020\u0014H\u0014J\b\u0010\u001e\u001a\u00020\u0014H\u0016J\b\u0010\u001f\u001a\u00020\u0014H\u0014J\b\u0010 \u001a\u00020\u0014H\u0014J\u0010\u0010!\u001a\u00020\u00142\u0006\u0010\"\u001a\u00020\u001cH\u0014J\b\u0010#\u001a\u00020\u0014H\u0002J\b\u0010$\u001a\u00020\u0014H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001c\u0010\t\u001a\u0010\u0012\f\u0012\n \f*\u0004\u0018\u00010\u000b0\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001b\u0010\r\u001a\u00020\u000e8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0011\u0010\u0012\u001a\u0004\b\u000f\u0010\u0010\u00a8\u0006&"}, d2 = {"Lcom/example/tomenaguita/ui/auth/RegisterActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "binding", "Lcom/example/tomenaguita/databinding/ActivityRegisterBinding;", "geocodingJob", "Lkotlinx/coroutines/Job;", "googleMap", "Lcom/google/android/gms/maps/GoogleMap;", "locationPermissionLauncher", "Landroidx/activity/result/ActivityResultLauncher;", "", "kotlin.jvm.PlatformType", "viewModel", "Lcom/example/tomenaguita/viewmodel/AuthViewModel;", "getViewModel", "()Lcom/example/tomenaguita/viewmodel/AuthViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "centrarEnUbicacion", "", "doRegister", "geocodearCentro", "lat", "", "lng", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "onLowMemory", "onPause", "onResume", "onSaveInstanceState", "outState", "setupObservers", "solicitarCentrarUbicacion", "Companion", "app_debug"})
public final class RegisterActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.example.tomenaguita.databinding.ActivityRegisterBinding binding;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    @org.jetbrains.annotations.Nullable()
    private com.google.android.gms.maps.GoogleMap googleMap;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job geocodingJob;
    @org.jetbrains.annotations.NotNull()
    private final androidx.activity.result.ActivityResultLauncher<java.lang.String> locationPermissionLauncher = null;
    @org.jetbrains.annotations.NotNull()
    private static final com.google.android.gms.maps.model.LatLng BOGOTA_DEFAULT = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.example.tomenaguita.ui.auth.RegisterActivity.Companion Companion = null;
    
    public RegisterActivity() {
        super();
    }
    
    private final com.example.tomenaguita.viewmodel.AuthViewModel getViewModel() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override()
    protected void onResume() {
    }
    
    @java.lang.Override()
    protected void onPause() {
    }
    
    @java.lang.Override()
    public void onLowMemory() {
    }
    
    @java.lang.Override()
    protected void onSaveInstanceState(@org.jetbrains.annotations.NotNull()
    android.os.Bundle outState) {
    }
    
    @java.lang.Override()
    protected void onDestroy() {
    }
    
    private final void setupObservers() {
    }
    
    private final void solicitarCentrarUbicacion() {
    }
    
    private final void centrarEnUbicacion() {
    }
    
    private final void geocodearCentro(double lat, double lng) {
    }
    
    private final void doRegister() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/example/tomenaguita/ui/auth/RegisterActivity$Companion;", "", "()V", "BOGOTA_DEFAULT", "Lcom/google/android/gms/maps/model/LatLng;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}