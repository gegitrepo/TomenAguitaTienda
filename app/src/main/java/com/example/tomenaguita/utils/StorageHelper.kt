package com.example.tomenaguita.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.FileOutputStream

/*
 * Objeto de utilidad para operaciones con Firebase Storage y el sistema de archivos local.
 * Maneja la subida de imagenes de productos y perfiles de usuario a la nube, la eliminacion
 * de imagenes en Storage, y la creacion de URIs temporales para camara y bitmaps.
 */
object StorageHelper {

    // Instancia unica de FirebaseStorage reutilizada en todas las operaciones
    private val storage = FirebaseStorage.getInstance()

    /**
     * Sube la imagen de un producto a Firebase Storage en la ruta productos/{productoId}/imagen.jpg.
     *
     * Consume:
     *   - productoId: identificador unico del producto (usado como nombre de carpeta en Storage).
     *   - imageUri: URI local de la imagen seleccionada por el usuario.
     *   - context: contexto de Android para acceder al ContentResolver.
     *
     * Devuelve: URL publica de descarga de la imagen subida.
     * Lanza Exception si el stream de la imagen no puede abrirse.
     */
    suspend fun uploadProductImage(productoId: String, imageUri: Uri, context: Context): String {
        val ref = storage.reference.child("productos/$productoId/imagen.jpg")
        context.contentResolver.openInputStream(imageUri)?.use { stream ->
            ref.putStream(stream).await()
        } ?: throw Exception("No se pudo leer la imagen seleccionada")
        return ref.downloadUrl.await().toString()
    }

    /**
     * Sube la foto de perfil de un usuario a Firebase Storage en la ruta perfiles/{userId}/foto.jpg.
     *
     * Consume:
     *   - userId: identificador unico del usuario (UID de Firebase Auth).
     *   - imageUri: URI local de la imagen seleccionada por el usuario.
     *   - context: contexto de Android para acceder al ContentResolver.
     *
     * Devuelve: URL publica de descarga de la foto de perfil subida.
     * Lanza Exception si el stream de la imagen no puede abrirse.
     */
    suspend fun uploadProfileImage(userId: String, imageUri: Uri, context: Context): String {
        val ref = storage.reference.child("perfiles/$userId/foto.jpg")
        context.contentResolver.openInputStream(imageUri)?.use { stream ->
            ref.putStream(stream).await()
        } ?: throw Exception("No se pudo leer la imagen seleccionada")
        return ref.downloadUrl.await().toString()
    }

    /**
     * Elimina un archivo almacenado en Firebase Storage.
     *
     * Consume: storagePath — ruta relativa dentro del bucket de Storage
     *          (por ejemplo, "productos/abc123/imagen.jpg").
     */
    suspend fun deleteImage(storagePath: String) {
        storage.reference.child(storagePath).delete().await()
    }

    /**
     * Crea un archivo temporal vacio en la cache y devuelve su URI compatible con la camara.
     * Usa FileProvider para exponer el archivo de forma segura a otras apps (Intent de camara).
     *
     * Consume: context — contexto de Android para acceder al directorio de cache y al FileProvider.
     * Devuelve: URI del archivo temporal listo para pasarlo como extra en un Intent de camara.
     */
    fun createImageUri(context: Context): Uri {
        // El nombre incluye el timestamp para evitar colisiones entre capturas
        val file = File(context.cacheDir, "camera_${System.currentTimeMillis()}.jpg")
        return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    }

    /**
     * Convierte un Bitmap en memoria a un archivo JPEG temporal en la cache
     * y devuelve su URI para poder usarla en operaciones de subida.
     *
     * Consume:
     *   - bitmap: imagen en memoria que se desea persistir temporalmente.
     *   - context: contexto de Android para acceder al directorio de cache.
     *
     * Devuelve: URI del archivo JPEG temporal (calidad 85 %).
     */
    fun bitmapToUri(bitmap: Bitmap, context: Context): Uri {
        val file = File(context.cacheDir, "temp_img_${System.currentTimeMillis()}.jpg")
        // Comprime el bitmap como JPEG con calidad 85 para equilibrar tamano y fidelidad
        FileOutputStream(file).use { bitmap.compress(Bitmap.CompressFormat.JPEG, 85, it) }
        return Uri.fromFile(file)
    }
}
