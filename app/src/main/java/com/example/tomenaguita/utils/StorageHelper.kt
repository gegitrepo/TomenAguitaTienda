package com.example.tomenaguita.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.FileOutputStream

object StorageHelper {

    private val storage = FirebaseStorage.getInstance()

    suspend fun uploadProductImage(productoId: String, imageUri: Uri, context: Context): String {
        val ref = storage.reference.child("productos/$productoId/imagen.jpg")
        context.contentResolver.openInputStream(imageUri)?.use { stream ->
            ref.putStream(stream).await()
        } ?: throw Exception("No se pudo leer la imagen seleccionada")
        return ref.downloadUrl.await().toString()
    }

    suspend fun uploadProfileImage(userId: String, imageUri: Uri, context: Context): String {
        val ref = storage.reference.child("perfiles/$userId/foto.jpg")
        context.contentResolver.openInputStream(imageUri)?.use { stream ->
            ref.putStream(stream).await()
        } ?: throw Exception("No se pudo leer la imagen seleccionada")
        return ref.downloadUrl.await().toString()
    }

    suspend fun deleteImage(storagePath: String) {
        storage.reference.child(storagePath).delete().await()
    }

    fun createImageUri(context: Context): Uri {
        val file = File(context.cacheDir, "camera_${System.currentTimeMillis()}.jpg")
        return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    }

    fun bitmapToUri(bitmap: Bitmap, context: Context): Uri {
        val file = File(context.cacheDir, "temp_img_${System.currentTimeMillis()}.jpg")
        FileOutputStream(file).use { bitmap.compress(Bitmap.CompressFormat.JPEG, 85, it) }
        return Uri.fromFile(file)
    }
}
