# FASE FINAL — Plan de implementacion para Claude Code

> **Proyecto:** TomenAguita — Tienda de Agua Embotellada
> **Ruta del proyecto:** `C:\Users\ASUS\Desktop\appMovil\Tomen aguita\`
> **Package:** `com.example.tomenaguita`
> **Objetivo:** Convertir la app (actualmente funcional solo con Room/SQLite local) en un aplicativo completamente funcional con Firebase, geolocalizacion y pasarela de pagos.

---

## ESTADO ACTUAL DEL PROYECTO

### Lo que YA funciona (no romper):
- Arquitectura MVVM completa con ViewModels, Repositories, DAOs y Room
- Autenticacion local: login con SHA-256, registro (solo rol comprador), sesion cifrada con EncryptedSharedPreferences
- Autenticacion biometrica con BiometricHelper (wrapper de BiometricPrompt)
- CRUD de usuarios (admin): ListaUsuariosFragment, CrearUsuarioFragment, EditarUsuarioFragment
- CRUD de productos (vendedor): CrearEditarProductoFragment, MisProductosFragment
- CRUD de carrito: CarritoFragment con agregar, eliminar, actualizar cantidad, vaciar
- Flujo de pedidos: ResumenPedidoFragment -> PasarelaPagoFragment -> HistorialPedidosFragment
- Navegacion por roles con NavGraph independientes y SafeArgs
- 3 Activities contenedoras: CompradorMainActivity (BottomNav 5 tabs), VendedorMainActivity (BottomNav 3 tabs), AdminMainActivity (DrawerLayout)
- SplashActivity con enrutamiento automatico por rol
- ViewBinding en todos los Fragments/Activities

### Lo que FALTA implementar:
1. **Firebase (Firestore + Auth + Storage)** — No hay NADA de Firebase en el proyecto
2. **Geolocalizacion** — Dependencias existen pero no hay codigo que las use
3. **Pasarela de pagos** — PasarelaPagoFragment es un stub (solo muestra Snackbar)
4. **Comentarios KDoc** — El codigo no tiene comentarios explicativos
5. **Git** — No hay repositorio inicializado

---

## REGLAS CRITICAS

1. **NO eliminar Room/SQLite.** Room se mantiene como cache offline. Firestore se agrega como fuente de verdad remota.
2. **NO cambiar la arquitectura MVVM ni la estructura de paquetes existente.** Se agregan archivos nuevos, se modifican los existentes.
3. **NO usar Jetpack Compose.** Todo es XML Views con ViewBinding.
4. **NO romper la navegacion existente.** Los NavGraphs y SafeArgs deben seguir funcionando.
5. **Kotlin idiomatico:** corrutinas, Flow, extension functions.
6. **Cada fase debe compilar sin errores antes de pasar a la siguiente.**

---

## FASE 1: Configurar Firebase en el proyecto

### Prerequisito manual (lo hace el usuario):
El usuario ya habra creado el proyecto en Firebase Console y colocado `google-services.json` en `app/`. **Antes de ejecutar esta fase, verificar que el archivo `app/google-services.json` existe.** Si no existe, indicar al usuario que lo cree.

### Tareas:

#### 1.1 Agregar plugin google-services al build system

**Archivo:** `build.gradle.kts` (raiz del proyecto)
```kotlin
// Agregar el plugin google-services
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.navigation.safeargs) apply false
    id("com.google.gms.google-services") version "4.4.0" apply false  // AGREGAR
}
```

**Archivo:** `app/build.gradle.kts`
```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.navigation.safeargs)
    id("com.google.gms.google-services")  // AGREGAR
}
```

#### 1.2 Agregar dependencias de Firebase

**Archivo:** `gradle/libs.versions.toml` — agregar en [versions]:
```toml
firebaseBom = "32.7.0"
```

**Archivo:** `gradle/libs.versions.toml` — agregar en [libraries]:
```toml
firebase-bom = { group = "com.google.firebase", name = "firebase-bom", version.ref = "firebaseBom" }
firebase-auth = { group = "com.google.firebase", name = "firebase-auth-ktx" }
firebase-firestore = { group = "com.google.firebase", name = "firebase-firestore-ktx" }
firebase-storage = { group = "com.google.firebase", name = "firebase-storage-ktx" }
```

**Archivo:** `app/build.gradle.kts` — agregar en dependencies:
```kotlin
implementation(platform(libs.firebase.bom))
implementation(libs.firebase.auth)
implementation(libs.firebase.firestore)
implementation(libs.firebase.storage)
```

#### 1.3 Verificacion
La app debe compilar sin errores. No debe haber cambios funcionales visibles todavia.

---

## FASE 2: Migrar autenticacion a Firebase Auth

### Contexto actual:
- `AuthViewModel.kt` usa `UsuarioRepository` -> `UsuarioDao` -> Room para login/registro
- Login: compara email + SHA-256(password) contra tabla `usuarios`
- Registro: inserta en Room con rol fijo "comprador"
- `SessionManager.kt` guarda sesion local con EncryptedSharedPreferences
- `ForgotPasswordActivity.kt` existe pero probablemente es solo UI sin logica real

### Archivos a crear:

#### 2.1 `data/repository/FirebaseAuthRepository.kt`
Nuevo repositorio que encapsula Firebase Auth:
- `suspend fun register(email, password): Result<FirebaseUser>` — usa `createUserWithEmailAndPassword`
- `suspend fun login(email, password): Result<FirebaseUser>` — usa `signInWithEmailAndPassword`
- `suspend fun sendPasswordReset(email): Result<Unit>` — usa `sendPasswordResetEmail`
- `fun logout()` — usa `FirebaseAuth.signOut()`
- `fun getCurrentUser(): FirebaseUser?`

#### 2.2 Modificar `AuthViewModel.kt`
- Inyectar `FirebaseAuthRepository` ademas de `UsuarioRepository`
- `login()`: primero autenticar con Firebase Auth, luego buscar/crear el usuario en Firestore (coleccion `usuarios`), guardar tambien en Room local como cache, y guardar sesion en SessionManager
- `register()`: crear usuario en Firebase Auth, luego crear documento en Firestore coleccion `usuarios` con los campos (nombre, email, telefono, rol, activo, fotoUrl, etc.), y tambien insertar en Room local
- Mantener la funcion `sha256()` para la compatibilidad con Room local

#### 2.3 Modificar `ForgotPasswordActivity.kt`
- Agregar un campo de email y boton
- Al presionar el boton, llamar `FirebaseAuth.sendPasswordResetEmail(email)`
- Mostrar mensaje de exito o error

#### 2.4 Modificar `LoginActivity.kt`
- El flujo de login ahora pasa por Firebase Auth primero
- Si Firebase Auth es exitoso, buscar el documento del usuario en Firestore para obtener el rol
- Guardar sesion en SessionManager como antes
- La autenticacion biometrica sigue igual (BiometricHelper no cambia, solo se usa para desbloqueo rapido cuando ya hay sesion guardada)

#### 2.5 Modificar logica de logout
- En los fragments/activities que tengan logout, agregar `FirebaseAuth.getInstance().signOut()` antes de `SessionManager.clearSession()`

### Estructura de documento en Firestore — coleccion `usuarios`:
```
usuarios/{firestoreDocId}
  ├── nombre: String
  ├── email: String
  ├── telefono: String
  ├── rol: String ("comprador" | "vendedor" | "administrador")
  ├── activo: Boolean
  ├── fotoUrl: String?
  ├── direccion: String?
  ├── latitud: Double?
  ├── longitud: Double?
  ├── biometricEnabled: Boolean
  ├── createdAt: Timestamp
  └── updatedAt: Timestamp
```

### Datos demo:
Crear los 3 usuarios de prueba en Firebase Auth y en Firestore:
- admin@tomenaguita.com / Admin123! (rol: administrador)
- vendedor@tomenaguita.com / Vendedor123! (rol: vendedor)
- comprador@tomenaguita.com / Comprador123! (rol: comprador)

Esto se puede hacer con un metodo `seedDemoData()` que se ejecute una sola vez si Firestore esta vacia.

---

## FASE 3: Migrar CRUDs a Firestore

### Estrategia:
Crear repositorios Firestore nuevos (`Firestore*Repository`) que convivan con los repositorios Room existentes. Los ViewModels usaran los repositorios Firestore como fuente primaria y Room como cache local.

### 3.1 CRUD de Usuarios (Admin)

**Crear:** `data/repository/FirestoreUsuarioRepository.kt`
- `fun getAllUsuarios(): Flow<List<Usuario>>` — escucha la coleccion `usuarios` con `snapshotFlow`
- `suspend fun insert(usuario: Usuario): String` — agrega documento a Firestore
- `suspend fun update(docId: String, campos: Map<String, Any>)` — actualiza documento
- `suspend fun desactivar(docId: String)` — cambia `activo = false`

**Modificar:** `UsuarioViewModel.kt`
- Reemplazar `UsuarioRepository` (Room) por `FirestoreUsuarioRepository` como fuente primaria
- Opcionalmente mantener Room como cache: al recibir datos de Firestore, guardarlos en Room

**Modificar fragments:**
- `ListaUsuariosFragment.kt` — ya observa LiveData, no deberia necesitar cambios si el ViewModel mantiene la misma API
- `CrearUsuarioFragment.kt` — al crear usuario, tambien crearlo en Firebase Auth (para que pueda hacer login)
- `EditarUsuarioFragment.kt` — actualizar en Firestore en vez de Room

**IMPORTANTE:** La entidad `Usuario.kt` necesitara un campo adicional `firestoreDocId: String?` para mapear el ID de Room con el ID de documento de Firestore. O alternativamente usar el email como clave primaria en Firestore (ya que es unico).

### 3.2 CRUD de Productos

**Crear:** `data/repository/FirestoreProductoRepository.kt`
- `fun getAllDisponibles(): Flow<List<Producto>>` — escucha coleccion `productos` donde `disponible == true` y `eliminado == false`
- `fun getByVendedor(vendedorId: String): Flow<List<Producto>>`
- `suspend fun insert(producto: Producto): String`
- `suspend fun update(docId: String, campos: Map<String, Any>)`
- `suspend fun softDelete(docId: String)`

**Estructura Firestore — coleccion `productos`:**
```
productos/{docId}
  ├── nombre: String
  ├── descripcion: String
  ├── presentacion: String
  ├── precio: Double
  ├── imagenUrl: String?
  ├── disponible: Boolean
  ├── stock: Int
  ├── vendedorId: String
  ├── eliminado: Boolean
  ├── createdAt: Timestamp
  └── updatedAt: Timestamp
```

**Modificar:** `ProductoViewModel.kt` — usar FirestoreProductoRepository

**Seed datos demo:** Crear los 8 productos del catalogo en Firestore al primer arranque.

### 3.3 CRUD de Carrito y Pedidos

**Crear:** `data/repository/FirestoreCarritoRepository.kt`
- Coleccion: `carritos/{usuarioId}/items/{itemId}`
- Mismas operaciones que CarritoRepository actual

**Crear:** `data/repository/FirestorePedidoRepository.kt`
- Coleccion: `pedidos/{pedidoId}` con subcoleccion `detalles/{detalleId}`
- `fun getPedidosByUsuario(usuarioId: String): Flow<List<Pedido>>`
- `fun getAllPedidos(): Flow<List<Pedido>>`
- `suspend fun crearPedido(pedido, detalles): String`
- `suspend fun actualizarEstado(docId: String, nuevoEstado: String)`

**Estructura Firestore — coleccion `pedidos`:**
```
pedidos/{docId}
  ├── orderNumber: String ("TA-YYYYMMDD-XXXX")
  ├── usuarioId: String
  ├── totalProductos: Double
  ├── costoEnvio: Double
  ├── totalPedido: Double
  ├── direccionEntrega: String
  ├── latitud: Double?
  ├── longitud: Double?
  ├── estado: String
  ├── metodoPago: String
  ├── transactionId: String?
  ├── createdAt: Timestamp
  └── updatedAt: Timestamp

pedidos/{docId}/detalles/{detalleId}
  ├── productoId: String
  ├── nombreProducto: String
  ├── presentacion: String
  ├── cantidad: Int
  ├── precioUnitario: Double
  ├── subtotal: Double
  └── vendedorId: String
```

**Modificar:** `CarritoViewModel.kt` y `PedidoViewModel.kt` — usar repositorios Firestore

---

## FASE 4: Imagenes con Firebase Storage

### Contexto:
- `CrearEditarProductoFragment.kt` ya tiene logica de camara/galeria
- `EditarPerfilFragment.kt` tambien
- Las imagenes actualmente se guardan como ruta local o no se guardan

### Tareas:

#### 4.1 Crear `utils/StorageHelper.kt`
Clase utilitaria que encapsula la subida a Firebase Storage:
- `suspend fun uploadProductImage(productoId: String, imageUri: Uri): String` — sube a `productos/{productoId}/imagen.jpg`, retorna la download URL
- `suspend fun uploadProfileImage(userId: String, imageUri: Uri): String` — sube a `perfiles/{userId}/foto.jpg`, retorna la download URL
- `suspend fun deleteImage(storagePath: String)` — elimina imagen

#### 4.2 Modificar `CrearEditarProductoFragment.kt`
- Despues de capturar/seleccionar imagen, llamar a `StorageHelper.uploadProductImage()`
- Guardar la URL retornada en el campo `imagenUrl` del documento Firestore del producto
- Mostrar la imagen con Glide (ya esta integrado, solo pasar la URL)

#### 4.3 Modificar `EditarPerfilFragment.kt`
- Mismo flujo: capturar foto -> subir a Storage -> guardar URL en Firestore -> mostrar con Glide

---

## FASE 5: Geolocalizacion

### Contexto:
- Dependencias `play-services-location` (21.1.0) y `play-services-maps` (18.2.0) ya estan en build.gradle
- `ResumenPedidoFragment.kt` actualmente muestra `demo_address` hardcodeada
- La entidad `Pedido` ya tiene campos `latitud` y `longitud`
- `AndroidManifest.xml` ya debe tener permisos ACCESS_FINE_LOCATION y ACCESS_COARSE_LOCATION

### Tareas:

#### 5.1 Crear `utils/LocationHelper.kt`
Clase utilitaria:
- `suspend fun getLastLocation(context: Context): Location?` — usa `FusedLocationProviderClient.lastLocation` con `await()`
- `suspend fun getAddressFromLocation(context: Context, lat: Double, lng: Double): String` — usa `Geocoder` para geocodificacion inversa, retorna la direccion como String legible

#### 5.2 Modificar `ResumenPedidoFragment.kt`
- Al entrar al fragment, solicitar permiso de ubicacion en runtime (ActivityResultContracts.RequestPermission)
- Si se concede: obtener ubicacion con LocationHelper, convertir a direccion con Geocoder, mostrar en `tvDireccion`
- Si se deniega: permitir al usuario escribir la direccion manualmente
- Guardar latitud y longitud en el pedido cuando se cree

#### 5.3 (Opcional) Agregar MapView
- En el layout `fragment_resumen_pedido.xml`, agregar un `MapView` o `SupportMapFragment` pequeno debajo de la direccion
- Mostrar un marcador en la ubicacion obtenida
- Requiere API key de Google Maps en `AndroidManifest.xml` (meta-data `com.google.android.geo.API_KEY`)
- **NOTA:** El usuario debera habilitar Maps SDK en Google Cloud Console y obtener la API key

---

## FASE 6: Pasarela de pagos (mercadopago)

### Contexto:
- `PasarelaPagoFragment.kt` es un stub que solo tiene un RadioButton y un boton que muestra Snackbar
- La entidad `Pedido` ya tiene campo `transactionId`
- El usuario tiene cuenta en Mercado Pago (Colombia)

### Estrategia:
Usar Mercado Pago Checkout Pro via WebView. Mercado Pago ofrece un entorno sandbox (de pruebas) que simula pagos sin cobrar dinero real. El flujo es: la app genera una preferencia de pago, abre el checkout de Mercado Pago en un WebView, el usuario completa el pago (con datos de prueba), y al finalizar la app captura el resultado.

### Prerequisito manual:
El usuario ya tiene cuenta en Mercado Pago. Debe ir a https://www.mercadopago.com.co/developers y obtener:
- `Access Token` de pruebas (sandbox) — se encuentra en "Tus integraciones" > crear aplicacion > credenciales de prueba
- `Public Key` de pruebas

**IMPORTANTE:** Usar SIEMPRE las credenciales de PRUEBA (sandbox), nunca las de produccion.

### Tareas:

#### 6.1 Agregar dependencia HTTP client

**Archivo:** `app/build.gradle.kts` — agregar en dependencies:
```kotlin
implementation("com.squareup.okhttp3:okhttp:4.12.0")
implementation("org.json:json:20231013")
```

OkHttp se usara para crear la preferencia de pago contra la API de Mercado Pago desde la app (en un proyecto real esto se haria desde un backend, pero para el proyecto academico es aceptable).

#### 6.2 Crear `utils/MercadoPagoHelper.kt`
Clase utilitaria que encapsula la integracion:
- `suspend fun crearPreferencia(accessToken: String, titulo: String, cantidad: Int, precioUnitario: Double, emailComprador: String, referenciaExterna: String): String`
  - Hace POST a `https://api.mercadopago.com/checkout/preferences`
  - Body JSON con: items (titulo, cantidad, precio), payer (email), external_reference (orderNumber), back_urls (success, failure, pending)
  - Retorna la `init_point` (URL del checkout) o `sandbox_init_point` para modo test
- Constante `SANDBOX_ACCESS_TOKEN` (el usuario la proporcionara)

#### 6.3 Modificar layout `fragment_pasarela_pago.xml`
- Reemplazar el RadioGroup actual por:
  - Resumen del monto a pagar (total, numero de pedido)
  - Boton "Pagar con Mercado Pago" (con el logo/colores de MP: fondo celeste #00AEEF)
  - Un `WebView` (inicialmente con visibility GONE) que se mostrara al abrir el checkout
  - Un `ProgressBar` para la carga

#### 6.4 Modificar `PasarelaPagoFragment.kt`
- Recibir via SafeArgs: monto total, orderNumber, email del comprador
- Al presionar "Pagar con Mercado Pago":
  1. Mostrar ProgressBar
  2. Llamar a `MercadoPagoHelper.crearPreferencia()` en una corrutina
  3. Con la URL obtenida (`sandbox_init_point`), cargarla en el WebView
  4. Configurar `WebViewClient` para interceptar las back_urls:
     - Si la URL contiene "success": pago exitoso → guardar el `payment_id` como `transactionId` en Firestore, cambiar estado del pedido a "PAGADO", mostrar confirmacion, navegar al historial
     - Si contiene "failure": pago fallido → mostrar error, mantener estado "PENDIENTE"
     - Si contiene "pending": pago pendiente → actualizar estado a "PROCESANDO"
  5. Configurar back_urls como:
     - success: `https://tomenaguita.app/pago/success`
     - failure: `https://tomenaguita.app/pago/failure`
     - pending: `https://tomenaguita.app/pago/pending`
     (Estas URLs no existen realmente, solo se usan para interceptar la redireccion en el WebViewClient)

#### 6.5 Datos de prueba Mercado Pago sandbox:
```
Tarjeta Mastercard: 5254 1336 7440 3564
CVV: 123
Fecha: 11/30 

Titular: APRO (para pago aprobado) / OTHE (para rechazado)
DNI/Documento: 123456789
```
```
Tarjeta Visa: 4013 5406 8274 6260
CVV: 123
Fecha: 11/30

Titular: APRO (para pago aprobado) / OTHE (para rechazado)
DNI/Documento: 123456789
```
```
Tarjeta Visa Debito: 4915 1120 5524 6507
CVV: 123
Fecha: 11/30

Titular: APRO (para pago aprobado) / OTHE (para rechazado)
DNI/Documento: 123456789
```

Mercado Pago en sandbox usa usuarios de prueba. El usuario puede crear compradores y vendedores de prueba desde el panel de desarrolladores.

#### 6.5.1 Cuentas de prueba Mercado Pago sandbox:
```
Seller Test User Vendedor

User ID: 3410484757
Usuario: TESTUSER8879678697068368906
Contraseña: 3dyhHutz5P
Código de verificación: 484757

Buyer Test User Comprador

User ID: 3410484759
Usuario: TESTUSER1685025191177064552
Contraseña: 9FzRrFjciM
Código de verificación: 484759
```

#### 6.6 Constantes
Guardar las credenciales en `utils/Constants.kt`:
```kotlin
const val MP_SANDBOX_ACCESS_TOKEN = "APP_USR-7127794535848181-051821-942b17d70ca66e01cb8c3db5a71d068d-3410484757"
const val MP_SANDBOX_PUBLIC_KEY = "APP_USR-55c98dba-38ca-4003-af98-a187f40b6182"
```
Las credenciales de sandbox ya estan incluidas arriba. Son de PRUEBA, no de produccion.

#### 6.7 Alternativa si Mercado Pago presenta problemas:
Si la integracion con Mercado Pago resulta muy compleja, implementar una pasarela simulada robusta que:
- Muestre un formulario de tarjeta (numero, fecha, CVC) con validacion de formato Luhn
- Simule un tiempo de procesamiento (ProgressBar 2-3 segundos)
- Genere un `transactionId` ficticio (UUID)
- Actualice el estado del pedido a "PAGADO" en Firestore
- Muestre confirmacion con el numero de transaccion
Esta alternativa es MENOS deseable pero aceptable si Mercado Pago presenta problemas tecnicos.

---

## FASE 7: Comentarios KDoc en todo el codigo

### Reglas:
- Cada archivo `.kt` debe tener un comentario de cabecera explicando el proposito de la clase
- Cada metodo publico debe tener KDoc (`/** */`) con descripcion, @param y @return donde aplique
- Cada propiedad publica relevante debe tener un comentario breve
- Los comentarios deben estar en **espanol** (es un proyecto academico colombiano)
- No comentar getters/setters triviales ni imports

### Ejemplo de formato:
```kotlin
/**
 * Repositorio que gestiona las operaciones de autenticacion
 * contra Firebase Auth y la persistencia del perfil de usuario
 * en Firestore.
 *
 * Actua como intermediario entre el ViewModel de autenticacion
 * y los servicios de Firebase.
 */
class FirebaseAuthRepository {

    /**
     * Registra un nuevo usuario en Firebase Auth y crea su
     * documento de perfil en la coleccion 'usuarios' de Firestore.
     *
     * @param email Correo electronico del usuario
     * @param password Contrasena en texto plano (Firebase la hashea internamente)
     * @return Result con el FirebaseUser creado o la excepcion
     */
    suspend fun register(email: String, password: String): Result<FirebaseUser> { ... }
}
```

### Archivos a comentar (todos los .kt del proyecto):
- Entities: Usuario.kt, Producto.kt, CarritoItem.kt, Pedido.kt, DetallePedido.kt
- DAOs: UsuarioDao.kt, ProductoDao.kt, CarritoDao.kt, PedidoDao.kt
- Database: AppDatabase.kt
- Repositories (Room): UsuarioRepository.kt, ProductoRepository.kt, CarritoRepository.kt, PedidoRepository.kt
- Repositories (Firebase — creados en fases 2-3): FirebaseAuthRepository.kt, FirestoreUsuarioRepository.kt, FirestoreProductoRepository.kt, FirestoreCarritoRepository.kt, FirestorePedidoRepository.kt
- ViewModels: AuthViewModel.kt, ProductoViewModel.kt, CarritoViewModel.kt, UsuarioViewModel.kt, PedidoViewModel.kt
- Utils: SessionManager.kt, BiometricHelper.kt, Constants.kt, Extensions.kt, StorageHelper.kt, LocationHelper.kt
- Enums: Rol.kt, EstadoPedido.kt
- Activities: SplashActivity.kt, LoginActivity.kt, RegisterActivity.kt, ForgotPasswordActivity.kt, CompradorMainActivity.kt, VendedorMainActivity.kt, AdminMainActivity.kt
- Adapters: ProductoAdapter.kt, CarritoAdapter.kt, PedidoAdapter.kt, UsuarioAdapter.kt, ProductoItem.kt
- Todos los Fragments (20+)

---

## FASE 8: Inicializar Git y subir a GitHub

### 8.1 Crear `.gitignore`
```
*.iml
.gradle/
/local.properties
/.idea/
/build/
/app/build/
/app/google-services.json
*.apk
*.aab
/captures
.externalNativeBuild/
.cxx/
local.properties
```

### 8.2 Inicializar repositorio
```bash
cd "C:\Users\ASUS\Desktop\appMovil\Tomen aguita"
git init
git add .
git commit -m "feat: proyecto TomenAguita completo con Firebase, geolocalizacion y pasarela de pagos"
```

### 8.3 Subir a GitHub
El usuario creara el repositorio en github.com y proporcionara la URL. Luego:
```bash
git remote add origin https://github.com/USUARIO/TomenAguitaTienda.git
git branch -M main
git push -u origin main
```

---

## RESUMEN DE ARCHIVOS NUEVOS A CREAR

```
data/repository/
  ├── FirebaseAuthRepository.kt          (Fase 2)
  ├── FirestoreUsuarioRepository.kt      (Fase 3)
  ├── FirestoreProductoRepository.kt     (Fase 3)
  ├── FirestoreCarritoRepository.kt      (Fase 3)
  └── FirestorePedidoRepository.kt       (Fase 3)

utils/
  ├── StorageHelper.kt                   (Fase 4)
  └── LocationHelper.kt                  (Fase 5)
```

## RESUMEN DE ARCHIVOS A MODIFICAR

```
build.gradle.kts (raiz)                  — agregar google-services plugin (Fase 1)
app/build.gradle.kts                     — agregar firebase deps + plugin (Fase 1)
gradle/libs.versions.toml               — agregar versiones firebase (Fase 1)
viewmodel/AuthViewModel.kt              — usar FirebaseAuth (Fase 2)
ui/auth/LoginActivity.kt                — login via Firebase (Fase 2)
ui/auth/RegisterActivity.kt             — registro via Firebase (Fase 2)
ui/auth/ForgotPasswordActivity.kt       — reseteo real con Firebase (Fase 2)
viewmodel/UsuarioViewModel.kt           — usar FirestoreUsuarioRepository (Fase 3)
viewmodel/ProductoViewModel.kt          — usar FirestoreProductoRepository (Fase 3)
viewmodel/CarritoViewModel.kt           — usar FirestoreCarritoRepository (Fase 3)
viewmodel/PedidoViewModel.kt            — usar FirestorePedidoRepository (Fase 3)
ui/admin/usuarios/*.kt                  — adaptar a Firestore IDs (Fase 3)
ui/vendedor/productos/CrearEditarProductoFragment.kt — subir imagen a Storage (Fase 4)
ui/comprador/perfil/EditarPerfilFragment.kt          — subir foto a Storage (Fase 4)
ui/comprador/pago/ResumenPedidoFragment.kt           — geolocalizacion (Fase 5)
res/layout/fragment_resumen_pedido.xml               — agregar MapView (Fase 5)
ui/comprador/pago/PasarelaPagoFragment.kt            — integracion ePayco (Fase 6)
res/layout/fragment_pasarela_pago.xml                — WebView para checkout (Fase 6)
Todos los .kt                                        — comentarios KDoc (Fase 7)
```

## ORDEN DE EJECUCION ESTRICTO

```
Fase 1 → Fase 2 → Fase 3 → Fase 4 → Fase 5 → Fase 6 → Fase 7 → Fase 8
```

**No saltar fases.** Cada fase depende de la anterior. Compilar y verificar despues de cada fase.
