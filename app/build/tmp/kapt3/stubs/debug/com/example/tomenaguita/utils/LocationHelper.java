package com.example.tomenaguita.utils;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\"\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\bH\u0002J&\u0010\n\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\rJ\u0018\u0010\u000e\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\u000b\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u0010\u0010\u00a8\u0006\u0011"}, d2 = {"Lcom/example/tomenaguita/utils/LocationHelper;", "", "()V", "formatAddress", "", "address", "Landroid/location/Address;", "lat", "", "lng", "getAddressFromLocation", "context", "Landroid/content/Context;", "(Landroid/content/Context;DDLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getLastLocation", "Landroid/location/Location;", "(Landroid/content/Context;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class LocationHelper {
    @org.jetbrains.annotations.NotNull()
    public static final com.example.tomenaguita.utils.LocationHelper INSTANCE = null;
    
    private LocationHelper() {
        super();
    }
    
    /**
     * Obtiene la ultima ubicacion conocida del dispositivo mediante FusedLocationProviderClient.
     *
     * Consume: context — contexto de Android necesario para acceder al servicio de ubicacion.
     * Devuelve: el objeto Location con las coordenadas, o null si no hay ubicacion disponible
     *          o si el permiso de ubicacion no ha sido concedido en tiempo de ejecucion.
     *
     * Requiere permiso ACCESS_FINE_LOCATION o ACCESS_COARSE_LOCATION concedido en runtime.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getLastLocation(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super android.location.Location> $completion) {
        return null;
    }
    
    /**
     * Convierte coordenadas geograficas (latitud/longitud) en una direccion postal legible.
     *
     * Consume:
     *  - context: contexto de Android para instanciar el Geocoder.
     *  - lat: latitud en grados decimales.
     *  - lng: longitud en grados decimales.
     *
     * Devuelve: cadena de texto con la direccion formateada. Si el geocoder falla,
     *          devuelve las coordenadas en formato "Lat: X, Lng: Y".
     *
     * Maneja la API sincrona (Android < 13) y la nueva API con callback (Android >= 13 / API 33).
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getAddressFromLocation(@org.jetbrains.annotations.NotNull()
    android.content.Context context, double lat, double lng, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.String> $completion) {
        return null;
    }
    
    /**
     * Construye una cadena de direccion legible a partir de un objeto Address del Geocoder.
     *
     * Consume:
     *  - address: objeto Address con los componentes de la direccion (puede ser null).
     *  - lat / lng: coordenadas de respaldo si address es null o esta vacio.
     *
     * Devuelve: cadena con calle, numero, localidad y departamento separados por comas,
     *          o las coordenadas si no se puede armar la direccion.
     */
    private final java.lang.String formatAddress(android.location.Address address, double lat, double lng) {
        return null;
    }
}