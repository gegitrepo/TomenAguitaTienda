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

object LocationHelper {

    /**
     * Obtiene la última ubicación conocida del dispositivo usando FusedLocationProviderClient.
     * Requiere permiso ACCESS_FINE_LOCATION o ACCESS_COARSE_LOCATION concedido en runtime.
     */
    suspend fun getLastLocation(context: Context): android.location.Location? = try {
        LocationServices.getFusedLocationProviderClient(context).lastLocation.await()
    } catch (_: Exception) {
        null
    }

    /**
     * Convierte coordenadas geográficas a una dirección legible usando Geocoder.
     * Maneja la API síncrona (< API 33) y la nueva API con callback (>= API 33).
     */
    suspend fun getAddressFromLocation(context: Context, lat: Double, lng: Double): String =
        withContext(Dispatchers.IO) {
            try {
                val geocoder = Geocoder(context, Locale.getDefault())
                val address: Address? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    suspendCancellableCoroutine { cont ->
                        geocoder.getFromLocation(lat, lng, 1) { addresses ->
                            cont.resume(addresses.firstOrNull())
                        }
                    }
                } else {
                    @Suppress("DEPRECATION")
                    geocoder.getFromLocation(lat, lng, 1)?.firstOrNull()
                }
                formatAddress(address, lat, lng)
            } catch (_: Exception) {
                "Lat: ${"%.5f".format(lat)}, Lng: ${"%.5f".format(lng)}"
            }
        }

    private fun formatAddress(address: Address?, lat: Double, lng: Double): String {
        if (address == null) return "Lat: ${"%.5f".format(lat)}, Lng: ${"%.5f".format(lng)}"
        return buildString {
            address.thoroughfare?.let { append(it) }
            address.subThoroughfare?.let {
                if (isNotEmpty()) append(" #$it") else append(it)
            }
            address.locality?.let {
                if (isNotEmpty()) append(", $it") else append(it)
            }
            address.adminArea?.let {
                if (isNotEmpty()) append(", $it") else append(it)
            }
        }.ifEmpty { "${address.countryName ?: "Lat: ${"%.5f".format(lat)}, Lng: ${"%.5f".format(lng)}"}" }
    }
}
