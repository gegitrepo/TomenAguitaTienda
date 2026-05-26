package com.example.tomenaguita.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.coroutines.resume

/*
 * Objeto de utilidad para operaciones de ubicacion y geocodificacion.
 * Proporciona funciones suspendidas para obtener la posicion GPS del dispositivo
 * y convertir coordenadas geograficas en direcciones legibles por el usuario.
 */
object LocationHelper {

    /**
     * Obtiene la ultima ubicacion conocida del dispositivo mediante FusedLocationProviderClient.
     *
     * Consume: context — contexto de Android necesario para acceder al servicio de ubicacion.
     * Devuelve: el objeto Location con las coordenadas, o null si no hay ubicacion disponible
     *           o si el permiso de ubicacion no ha sido concedido en tiempo de ejecucion.
     *
     * Requiere permiso ACCESS_FINE_LOCATION o ACCESS_COARSE_LOCATION concedido en runtime.
     */
    suspend fun getLastLocation(context: Context): android.location.Location? = try {
        LocationServices.getFusedLocationProviderClient(context).lastLocation.await()
    } catch (_: Exception) {
        null
    }

    /**
     * Convierte coordenadas geograficas (latitud/longitud) en una direccion postal legible.
     *
     * Consume:
     *   - context: contexto de Android para instanciar el Geocoder.
     *   - lat: latitud en grados decimales.
     *   - lng: longitud en grados decimales.
     *
     * Devuelve: cadena de texto con la direccion formateada. Si el geocoder falla,
     *           devuelve las coordenadas en formato "Lat: X, Lng: Y".
     *
     * Maneja la API sincrona (Android < 13) y la nueva API con callback (Android >= 13 / API 33).
     */
    suspend fun getAddressFromLocation(context: Context, lat: Double, lng: Double): String =
        withContext(Dispatchers.IO) {
            try {
                val geocoder = Geocoder(context, Locale.getDefault())

                // En Android 13+ se usa la API con callback para no bloquear el hilo
                val address: Address? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    suspendCancellableCoroutine { cont ->
                        geocoder.getFromLocation(lat, lng, 1) { addresses ->
                            cont.resume(addresses.firstOrNull())
                        }
                    }
                } else {
                    // En versiones anteriores se usa la API sincrona (marcada como deprecada en API 33)
                    @Suppress("DEPRECATION")
                    geocoder.getFromLocation(lat, lng, 1)?.firstOrNull()
                }

                formatAddress(address, lat, lng)
            } catch (_: Exception) {
                // Si el geocoder no esta disponible, devuelve las coordenadas en bruto
                "Lat: ${"%.5f".format(lat)}, Lng: ${"%.5f".format(lng)}"
            }
        }

    /**
     * Construye una cadena de direccion legible a partir de un objeto Address del Geocoder.
     *
     * Consume:
     *   - address: objeto Address con los componentes de la direccion (puede ser null).
     *   - lat / lng: coordenadas de respaldo si address es null o esta vacio.
     *
     * Devuelve: cadena con calle, numero, localidad y departamento separados por comas,
     *           o las coordenadas si no se puede armar la direccion.
     */
    private fun formatAddress(address: Address?, lat: Double, lng: Double): String {
        if (address == null) return "Lat: ${"%.5f".format(lat)}, Lng: ${"%.5f".format(lng)}"
        return buildString {
            // Nombre de la calle o via principal
            address.thoroughfare?.let { append(it) }
            // Numero de la via; se antepone "#" si ya hay contenido
            address.subThoroughfare?.let {
                if (isNotEmpty()) append(" #$it") else append(it)
            }
            // Ciudad o municipio
            address.locality?.let {
                if (isNotEmpty()) append(", $it") else append(it)
            }
            // Departamento o estado
            address.adminArea?.let {
                if (isNotEmpty()) append(", $it") else append(it)
            }
        }.ifEmpty {
            // Si el buildString quedo vacio, usa el nombre del pais o las coordenadas
            "${address.countryName ?: "Lat: ${"%.5f".format(lat)}, Lng: ${"%.5f".format(lng)}"}"
        }
    }
}
