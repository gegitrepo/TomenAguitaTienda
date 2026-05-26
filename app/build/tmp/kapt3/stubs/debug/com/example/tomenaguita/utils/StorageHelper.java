package com.example.tomenaguita.utils;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\b\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ\u000e\u0010\u000b\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\nJ\u0016\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0086@\u00a2\u0006\u0002\u0010\u0010J&\u0010\u0011\u001a\u00020\u000f2\u0006\u0010\u0012\u001a\u00020\u000f2\u0006\u0010\u0013\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\nH\u0086@\u00a2\u0006\u0002\u0010\u0014J&\u0010\u0015\u001a\u00020\u000f2\u0006\u0010\u0016\u001a\u00020\u000f2\u0006\u0010\u0013\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\nH\u0086@\u00a2\u0006\u0002\u0010\u0014R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2 = {"Lcom/example/tomenaguita/utils/StorageHelper;", "", "()V", "storage", "Lcom/google/firebase/storage/FirebaseStorage;", "bitmapToUri", "Landroid/net/Uri;", "bitmap", "Landroid/graphics/Bitmap;", "context", "Landroid/content/Context;", "createImageUri", "deleteImage", "", "storagePath", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "uploadProductImage", "productoId", "imageUri", "(Ljava/lang/String;Landroid/net/Uri;Landroid/content/Context;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "uploadProfileImage", "userId", "app_debug"})
public final class StorageHelper {
    @org.jetbrains.annotations.NotNull()
    private static final com.google.firebase.storage.FirebaseStorage storage = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.example.tomenaguita.utils.StorageHelper INSTANCE = null;
    
    private StorageHelper() {
        super();
    }
    
    /**
     * Sube la imagen de un producto a Firebase Storage en la ruta productos/{productoId}/imagen.jpg.
     *
     * Consume:
     *  - productoId: identificador unico del producto (usado como nombre de carpeta en Storage).
     *  - imageUri: URI local de la imagen seleccionada por el usuario.
     *  - context: contexto de Android para acceder al ContentResolver.
     *
     * Devuelve: URL publica de descarga de la imagen subida.
     * Lanza Exception si el stream de la imagen no puede abrirse.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object uploadProductImage(@org.jetbrains.annotations.NotNull()
    java.lang.String productoId, @org.jetbrains.annotations.NotNull()
    android.net.Uri imageUri, @org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.String> $completion) {
        return null;
    }
    
    /**
     * Sube la foto de perfil de un usuario a Firebase Storage en la ruta perfiles/{userId}/foto.jpg.
     *
     * Consume:
     *  - userId: identificador unico del usuario (UID de Firebase Auth).
     *  - imageUri: URI local de la imagen seleccionada por el usuario.
     *  - context: contexto de Android para acceder al ContentResolver.
     *
     * Devuelve: URL publica de descarga de la foto de perfil subida.
     * Lanza Exception si el stream de la imagen no puede abrirse.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object uploadProfileImage(@org.jetbrains.annotations.NotNull()
    java.lang.String userId, @org.jetbrains.annotations.NotNull()
    android.net.Uri imageUri, @org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.String> $completion) {
        return null;
    }
    
    /**
     * Elimina un archivo almacenado en Firebase Storage.
     *
     * Consume: storagePath — ruta relativa dentro del bucket de Storage
     *         (por ejemplo, "productos/abc123/imagen.jpg").
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object deleteImage(@org.jetbrains.annotations.NotNull()
    java.lang.String storagePath, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * Crea un archivo temporal vacio en la cache y devuelve su URI compatible con la camara.
     * Usa FileProvider para exponer el archivo de forma segura a otras apps (Intent de camara).
     *
     * Consume: context — contexto de Android para acceder al directorio de cache y al FileProvider.
     * Devuelve: URI del archivo temporal listo para pasarlo como extra en un Intent de camara.
     */
    @org.jetbrains.annotations.NotNull()
    public final android.net.Uri createImageUri(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    /**
     * Convierte un Bitmap en memoria a un archivo JPEG temporal en la cache
     * y devuelve su URI para poder usarla en operaciones de subida.
     *
     * Consume:
     *  - bitmap: imagen en memoria que se desea persistir temporalmente.
     *  - context: contexto de Android para acceder al directorio de cache.
     *
     * Devuelve: URI del archivo JPEG temporal (calidad 85 %).
     */
    @org.jetbrains.annotations.NotNull()
    public final android.net.Uri bitmapToUri(@org.jetbrains.annotations.NotNull()
    android.graphics.Bitmap bitmap, @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
}