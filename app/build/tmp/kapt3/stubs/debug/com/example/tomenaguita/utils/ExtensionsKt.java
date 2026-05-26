package com.example.tomenaguita.utils;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000\"\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0006\n\u0002\b\u0002\u001a\n\u0010\u0000\u001a\u00020\u0001*\u00020\u0002\u001a\n\u0010\u0003\u001a\u00020\u0001*\u00020\u0002\u001a\n\u0010\u0004\u001a\u00020\u0005*\u00020\u0006\u001a\n\u0010\u0007\u001a\u00020\u0005*\u00020\u0006\u001a\u0012\u0010\b\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\t\u001a\u00020\u0006\u001a\n\u0010\n\u001a\u00020\u0006*\u00020\u000b\u001a\n\u0010\f\u001a\u00020\u0001*\u00020\u0002\u00a8\u0006\r"}, d2 = {"gone", "", "Landroid/view/View;", "invisible", "isValidColombian", "", "", "isValidEmail", "showSnackbar", "message", "toCOP", "", "visible", "app_debug"})
public final class ExtensionsKt {
    
    /**
     * Formatea un valor Double como precio en pesos colombianos (COP) usando
     * el locale "es_CO", sin decimales (NumberFormat con maximumFractionDigits = 0).
     *
     * Devuelve: cadena con el simbolo de moneda y el valor separado por puntos,
     *          por ejemplo "$ 15.000".
     */
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String toCOP(double $this$toCOP) {
        return null;
    }
    
    /**
     * Muestra un Snackbar de duracion corta (LENGTH_SHORT) anclado a la View receptora.
     *
     * Consume: message — texto a mostrar en el Snackbar.
     */
    public static final void showSnackbar(@org.jetbrains.annotations.NotNull()
    android.view.View $this$showSnackbar, @org.jetbrains.annotations.NotNull()
    java.lang.String message) {
    }
    
    public static final void visible(@org.jetbrains.annotations.NotNull()
    android.view.View $this$visible) {
    }
    
    public static final void gone(@org.jetbrains.annotations.NotNull()
    android.view.View $this$gone) {
    }
    
    public static final void invisible(@org.jetbrains.annotations.NotNull()
    android.view.View $this$invisible) {
    }
    
    /**
     * Verifica si la cadena receptora tiene formato de correo electronico valido
     * usando el patron de Android (android.util.Patterns.EMAIL_ADDRESS).
     *
     * Devuelve: true si la cadena es un correo valido.
     */
    public static final boolean isValidEmail(@org.jetbrains.annotations.NotNull()
    java.lang.String $this$isValidEmail) {
        return false;
    }
    
    /**
     * Verifica si la cadena receptora es un numero de telefono movil colombiano valido.
     * El formato esperado es exactamente 10 digitos que comienzan con "3"
     * (operadores moviles colombianos: 300-329, 350-359, etc.).
     *
     * Devuelve: true si la cadena coincide con el patron de celular colombiano.
     */
    public static final boolean isValidColombian(@org.jetbrains.annotations.NotNull()
    java.lang.String $this$isValidColombian) {
        return false;
    }
}