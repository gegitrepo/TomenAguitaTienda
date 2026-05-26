# Tomen Agüita — Tienda de Agua Embotellada

> **Proyecto académico** — Fundación Universitaria Compensar · Desarrollo de Aplicaciones Móviles
>
> Esta aplicación fue desarrollada con fines estrictamente educativos como parte de una actividad académica.
> No constituye una tienda real. Las credenciales de pago son de entorno sandbox (prueba).

---

## Tabla de contenidos

1. [Descripción general](#descripción-general)
2. [Finalidad del proyecto](#finalidad-del-proyecto)
3. [Características principales](#características-principales)
4. [Roles de usuario](#roles-de-usuario)
5. [Arquitectura del proyecto](#arquitectura-del-proyecto)
6. [Capas de datos: Room + Firestore](#capas-de-datos-room--firestore)
7. [Flujo de pago con Stripe](#flujo-de-pago-con-stripe)
8. [Integración con Google Maps](#integración-con-google-maps)
9. [Jerarquía de archivos](#jerarquía-de-archivos)
10. [Diseño y paleta de colores](#diseño-y-paleta-de-colores)
11. [Bibliotecas utilizadas](#bibliotecas-utilizadas)
12. [Permisos del dispositivo](#permisos-del-dispositivo)
13. [Credenciales de demostración](#credenciales-de-demostración)
14. [Instalación y configuración](#instalación-y-configuración)
15. [Información académica](#información-académica)

---

## Descripción general

**Tomen Agüita** es una aplicación Android nativa para la gestión de una tienda de agua purificada embotellada en Colombia. Permite a compradores explorar y adquirir productos con pago real mediante Stripe, a vendedores gestionar el catálogo e inventario, y a administradores supervisar usuarios, productos y reportes de ventas.

La app usa Firebase como backend completo (Auth, Firestore, Storage), Google Maps para selección de dirección de entrega y Stripe PaymentSheet para el procesamiento de pagos.

| Dato | Valor |
|---|---|
| Plataforma | Android |
| Lenguaje | Kotlin |
| SDK mínimo | API 24 (Android 7.0 Nougat) |
| SDK objetivo | API 34 (Android 14) |
| Arquitectura | MVVM (Model – View – ViewModel) |
| ID de paquete | `com.example.tomenaguita` |
| Backend | Firebase (Auth + Firestore + Storage) |
| Pagos | Stripe SDK (PaymentSheet) |

---

## Finalidad del proyecto

El objetivo académico es demostrar el dominio integral del desarrollo de aplicaciones móviles nativas para Android, incluyendo:

- Diseño de interfaces con **XML Views** y **Material Design 3**
- Patrón **MVVM** con `AndroidViewModel`, `LiveData`, `Flow` y corrutinas
- Base de datos local con **Room / SQLite** como caché
- Base de datos en la nube con **Cloud Firestore** como persistencia real
- Autenticación de usuarios con **Firebase Auth**
- Almacenamiento de imágenes con **Firebase Storage**
- Navegación con **Jetpack Navigation Component** y **SafeArgs**
- Integración de pagos con **Stripe PaymentSheet**
- Mapas interactivos con **Google Maps SDK**
- Sesión segura con **EncryptedSharedPreferences**
- Autenticación biométrica con **BiometricPrompt**
- Control de acceso por **roles de usuario** (comprador, vendedor, administrador)
- Gestión de inventario en tiempo real
- Auditoría de código y eliminación de valores hardcodeados

---

## Características principales

### Autenticación
- Inicio de sesión con Firebase Auth (email + contraseña)
- Registro de nuevos usuarios con validación de correo y teléfono colombiano (10 dígitos, inicia en 3)
- Recuperación de contraseña por correo electrónico
- Autenticación biométrica con huella digital (dispositivos compatibles)
- Sesión persistente cifrada con `EncryptedSharedPreferences`
- Enrutamiento automático al módulo correcto según el rol al iniciar sesión

### Comprador
- Catálogo completo de productos con búsqueda en tiempo real
- Pantalla de inicio con productos destacados aleatorios
- Vista detallada de cada producto con imagen, descripción y selección de cantidad
- Carrito de compras con ajuste de cantidades y vaciado completo
- Resumen del pedido con selección de dirección mediante mapa interactivo
- Pasarela de pago con **Stripe PaymentSheet** (tarjeta de crédito/débito)
- Reducción automática de inventario al completar un pago exitoso
- Historial de pedidos con filtros por estado
- Detalle de cada pedido con opción de cancelar o reordenar
- Perfil editable con foto (cámara o galería), datos de contacto y dirección con mapa

### Vendedor
- Listado del catálogo completo con opciones de editar y eliminar
- Creación y edición de productos con imagen, descripción, precio, stock y disponibilidad
- Pedidos recibidos en tiempo real desde Firestore con filtros por estado
- Avance manual del estado del pedido: pagado → enviado → entregado
- Detalle del pedido con datos del comprador
- Perfil editable

### Administrador
- Dashboard con métricas en tiempo real: usuarios, productos, pedidos totales, ventas pagadas, pedidos pendientes y cancelados
- Gestión completa de usuarios: crear, editar, activar/desactivar, asignar rol y dirección con mapa
- Gestión del catálogo de productos: búsqueda, edición y eliminación
- Reporte de ventas con filtros por estado (todos, pagados, pendientes, cancelados)
- Métricas de ventas del día, del mes y totales (solo pedidos pagados)
- Conteo de ventas pendientes y canceladas

### Generales
- Sincronización en tiempo real entre Room y Firestore con listeners de Firestore
- Numeración de pedidos en formato `TA-YYYYMMDD-XXXX`
- Formato de precios en pesos colombianos (COP) con separador de miles
- Splash screen con animación
- Limpieza del carrito al completar un pago exitoso (Room + Firestore)

---

## Roles de usuario

| Rol | Actividad principal | Navegación | Pantallas |
|---|---|---|---|
| **Comprador** | `CompradorMainActivity` | `BottomNavigationView` (5 tabs) | Inicio · Catálogo · Carrito · Pedidos · Perfil |
| **Vendedor** | `VendedorMainActivity` | `BottomNavigationView` (3 tabs) | Mis Productos · Pedidos Recibidos · Perfil |
| **Administrador** | `AdminMainActivity` | `NavigationDrawer` + Toolbar | Dashboard · Usuarios · Productos · Reportes |

Cada rol tiene su propia `Activity` principal y su propio grafo de navegación (`NavGraph`). El enrutamiento se realiza en `SplashActivity` consultando el rol guardado en sesión.

---

## Arquitectura del proyecto

La aplicación sigue el patrón **MVVM** recomendado por Google con separación en tres capas:

```
┌────────────────────────────────────────────────────┐
│                    UI Layer                        │
│   Activities · Fragments · Adapters                │
│   Observan LiveData, llaman funciones del VM       │
├────────────────────────────────────────────────────┤
│                ViewModel Layer                     │
│   AndroidViewModel · LiveData · viewModelScope     │
│   Coordina Room + Firestore, expone estado a la UI │
├────────────────────────────────────────────────────┤
│                  Data Layer                        │
│   Repositories (Room) → DAOs → Room DB (SQLite)    │
│   Repositories (Firestore) → Cloud Firestore       │
└────────────────────────────────────────────────────┘
```

### Flujo de datos
```
Fragment → ViewModel → RoomRepository → DAO → Room DB (local)
                  ↑                                  |
             LiveData  ←──── Flow.asLiveData() ──────┘

Fragment → ViewModel → FirestoreRepository → Cloud Firestore
                  ↑                                  |
          MutableLiveData ←── callbackFlow / collect ┘
```

---

## Capas de datos: Room + Firestore

La app usa **dos capas de persistencia** con roles distintos:

| Capa | Tecnología | Rol |
|---|---|---|
| **Local** | Room (SQLite) | Caché para la UI; fuente de verdad para respuesta inmediata |
| **Nube** | Cloud Firestore | Persistencia real; sincronización entre dispositivos |

### Estrategia de sincronización

- **Compradores:** crean pedidos localmente en Room y los replican a Firestore.
- **Vendedores:** sus pedidos recibidos viven en Firestore; `getAllPedidosForVendedor()` los sincroniza a Room en tiempo real para que la navegación al detalle funcione.
- **Administradores:** Room del admin está vacía al instalar; `getAllPedidosForAdmin()` sincroniza desde Firestore sin detalles (suficiente para dashboard y reportes).
- **Productos:** se sincronizan de Firestore → Room en el `init` del `ProductoViewModel`. La vista del vendedor muestra todo el catálogo (sin filtro por `vendedorId`) porque los productos demo usan `vendedorId = "demo"`.

### Estructura de Firestore

```
usuarios/{uid}
  nombre, email, telefono, rol, activo, direccion, fotoUrl, createdAt, updatedAt

productos/{docId}
  nombre, descripcion, presentacion, precio, stock, disponible, vendedorId, eliminado, imagenUrl

pedidos/{orderNumber}
  orderNumber, usuarioId, usuarioUid, totalPedido, direccionEntrega, estado,
  metodoPago, transactionId, createdAt, updatedAt
  └── detalles/{detId}
        productoId, vendedorId, nombreProducto, presentacion, cantidad,
        precioUnitario, subtotal
```

---

## Flujo de pago con Stripe

La integración de pagos usa **Stripe SDK para Android** con el componente `PaymentSheet`:

```
Comprador confirma pedido
  → ResumenPedidoFragment crea el pedido en Room + Firestore
  → PasarelaPagoFragment llama a StripeHelper.createPaymentIntent()
       ↓ POST https://api.stripe.com/v1/payment_intents
       ↓ Retorna client_secret
  → PaymentSheet.presentWithPaymentIntent(client_secret)
       ↓ Formulario nativo de Stripe (tarjeta, nombre, fecha, CVV)
  → PaymentSheetResult.Completed
       → Actualiza estado del pedido a PAGADO en Room + Firestore
       → Guarda transactionId (pi_xxx) en Firestore
       → Vacía carrito en Room + Firestore
       → Reduce el stock de cada producto en Firestore
       → Navega al historial de pedidos
```

> **Nota de producción:** `StripeHelper` realiza la llamada a la API de Stripe directamente desde el cliente usando el `secret key`. Esto es aceptable **solo en entorno de prueba/sandbox** para facilitar el desarrollo académico. En producción, esta llamada debe realizarse desde un backend propio para no exponer el `secret key`.

### Tarjetas de prueba Stripe (sandbox)
| Tarjeta | Número | Resultado |
|---|---|---|
| Visa exitosa | `4242 4242 4242 4242` | Pago aprobado |
| Visa declinada | `4000 0000 0000 0002` | Pago rechazado |
| Fecha | Cualquier fecha futura | — |
| CVV | Cualquier 3 dígitos | — |

---

## Integración con Google Maps

Google Maps se usa en tres pantallas para seleccionar la dirección de entrega:

- **ResumenPedidoFragment:** el comprador arrastra el mapa o toca para colocar un marcador; al detener la cámara se geocodifica la posición automáticamente.
- **EditarPerfilFragment:** mismo comportamiento para guardar la dirección del perfil.
- **CrearUsuarioFragment (admin):** mismo comportamiento al crear un usuario nuevo.

Los mapas se inicializan con Bogotá como centro por defecto (`LatLng(4.7110, -74.0721)`) porque es el mercado objetivo del negocio.

---

## Jerarquía de archivos

```
app/src/main/
├── AndroidManifest.xml
│
├── java/com/example/tomenaguita/
│   │
│   ├── data/
│   │   ├── database/
│   │   │   ├── dao/
│   │   │   │   ├── CarritoDao.kt
│   │   │   │   ├── PedidoDao.kt
│   │   │   │   ├── ProductoDao.kt
│   │   │   │   └── UsuarioDao.kt
│   │   │   ├── entity/
│   │   │   │   ├── CarritoItem.kt
│   │   │   │   ├── DetallePedido.kt
│   │   │   │   ├── Pedido.kt
│   │   │   │   ├── Producto.kt
│   │   │   │   └── Usuario.kt
│   │   │   └── AppDatabase.kt              ← singleton Room v2
│   │   ├── model/
│   │   │   └── EstadoPedido.kt             ← enum: PENDIENTE→PAGADO→ENVIADO→ENTREGADO|CANCELADO
│   │   └── repository/
│   │       ├── CarritoRepository.kt        ← Room
│   │       ├── PedidoRepository.kt         ← Room
│   │       ├── ProductoRepository.kt       ← Room
│   │       ├── UsuarioRepository.kt        ← Room
│   │       ├── FirestoreCarritoRepository.kt
│   │       ├── FirestorePedidoRepository.kt
│   │       ├── FirestoreProductoRepository.kt
│   │       └── FirestoreUsuarioRepository.kt
│   │
│   ├── ui/
│   │   ├── adapter/
│   │   │   ├── CarritoAdapter.kt
│   │   │   ├── DetallePedidoAdapter.kt
│   │   │   ├── PedidoAdapter.kt
│   │   │   ├── ProductoAdapter.kt          ← parámetro showAgregarButton para admin
│   │   │   ├── ProductoItem.kt             ← data class UI separado de la entidad Room
│   │   │   └── UsuarioAdapter.kt
│   │   ├── admin/
│   │   │   ├── AdminMainActivity.kt        ← DrawerLayout + Toolbar
│   │   │   ├── dashboard/DashboardFragment.kt
│   │   │   ├── productos/GestionProductosFragment.kt
│   │   │   ├── reportes/ReporteVentasFragment.kt
│   │   │   └── usuarios/
│   │   │       ├── CrearUsuarioFragment.kt ← con MapView para dirección
│   │   │       ├── EditarUsuarioFragment.kt
│   │   │       └── ListaUsuariosFragment.kt
│   │   ├── auth/
│   │   │   ├── ForgotPasswordActivity.kt
│   │   │   ├── LoginActivity.kt
│   │   │   └── RegisterActivity.kt
│   │   ├── comprador/
│   │   │   ├── CompradorMainActivity.kt    ← BottomNavigationView (5 tabs)
│   │   │   ├── carrito/CarritoFragment.kt
│   │   │   ├── catalogo/CatalogoFragment.kt
│   │   │   ├── home/HomeFragment.kt
│   │   │   ├── pago/
│   │   │   │   ├── PasarelaPagoFragment.kt ← Stripe PaymentSheet
│   │   │   │   ├── PaymentResultActivity.kt← recibe deep link de resultado
│   │   │   │   └── ResumenPedidoFragment.kt← MapView para dirección de entrega
│   │   │   ├── pedidos/
│   │   │   │   ├── DetallePedidoFragment.kt
│   │   │   │   └── HistorialPedidosFragment.kt
│   │   │   ├── perfil/
│   │   │   │   ├── EditarPerfilFragment.kt ← MapView + foto cámara/galería
│   │   │   │   └── PerfilFragment.kt
│   │   │   └── producto/ProductoDetalleFragment.kt
│   │   ├── splash/SplashActivity.kt
│   │   └── vendedor/
│   │       ├── VendedorMainActivity.kt     ← BottomNavigationView (3 tabs)
│   │       ├── pedidos/
│   │       │   ├── DetallePedidoVendedorFragment.kt
│   │       │   └── PedidosRecibidosFragment.kt ← filtros por estado (chips)
│   │       ├── perfil/PerfilVendedorFragment.kt
│   │       └── productos/
│   │           ├── CrearEditarProductoFragment.kt
│   │           └── MisProductosFragment.kt
│   │
│   ├── utils/
│   │   ├── BiometricHelper.kt              ← wrapper de BiometricPrompt
│   │   ├── Constants.kt                    ← credenciales Stripe, config mapas, colecciones FS
│   │   ├── Extensions.kt                   ← toCOP(), showSnackbar(), isValidEmail()…
│   │   ├── LocationHelper.kt               ← FusedLocationProvider + Geocoder
│   │   ├── SessionManager.kt               ← EncryptedSharedPreferences
│   │   ├── StorageHelper.kt                ← subida de imágenes a Firebase Storage
│   │   └── StripeHelper.kt                 ← createPaymentIntent() → client_secret
│   │
│   └── viewmodel/
│       ├── AuthViewModel.kt
│       ├── CarritoViewModel.kt
│       ├── PedidoViewModel.kt              ← sincronización Firestore→Room para vendedor/admin
│       ├── ProductoViewModel.kt            ← sync Firestore→Room + reducirStock()
│       └── UsuarioViewModel.kt
│
├── res/
│   ├── drawable/       ← íconos SVG, fondos, selectores, pin de mapa
│   ├── layout/         ← activities, fragments, items de lista
│   ├── menu/           ← bottom_nav_comprador, bottom_nav_vendedor, drawer_admin
│   ├── mipmap-*/       ← ícono de lanzador (adaptativo API 26+)
│   ├── navigation/     ← nav_graph_comprador, nav_graph_vendedor, nav_graph_admin
│   ├── values/
│   │   ├── colors.xml
│   │   ├── dimens.xml
│   │   ├── strings.xml
│   │   └── themes.xml
│   └── xml/
│       └── file_paths.xml  ← rutas para FileProvider (fotos de cámara)
│
├── google-services.json    ← configuración de Firebase (NO versionar en producción)
├── gradle.properties       ← android.useAndroidX=true, jetifier=true
└── local.properties        ← sdk.dir (generado localmente, NO versionar)
```

---

## Diseño y paleta de colores

El diseño sigue las guías de **Material Design 3** con el tema base `Theme.Material3.DayNight.NoActionBar`.

### Paleta principal

| Nombre | Hex | Uso |
|---|---|---|
| `primary` | `#1565C0` | Botones principales, AppBar, elementos activos |
| `primary_dark` | `#0D47A1` | StatusBar, fondo splash |
| `primary_light` | `#42A5F5` | Highlights, iconos secundarios |
| `secondary` | `#00BCD4` | Acento, tarjetas de métricas |
| `accent` | `#26A69A` | Chips, indicadores |
| `success` | `#4CAF50` | Mensajes de éxito, estado entregado |
| `warning` | `#FFA000` | Estado pendiente, métricas de alerta |
| `error` | `#B00020` | Errores, estado cancelado |

### Paleta de estados de pedido

| Estado | Color | Hex |
|---|---|---|
| Pendiente | Naranja | `#FFA500` |
| Pagado | Azul | `#2196F3` |
| Enviado | Morado | `#9C27B0` |
| Entregado | Verde | `#4CAF50` |
| Cancelado | Rojo | `#F44336` |

### Colores sandbox (tarjeta de datos de prueba Stripe)

| Nombre | Hex | Uso |
|---|---|---|
| `sandbox_card_bg` | `#FFF3E0` | Fondo tarjeta de datos de prueba |
| `sandbox_card_title` | `#E65100` | Título tarjeta de datos de prueba |
| `sandbox_card_text` | `#BF360C` | Texto tarjeta de datos de prueba |

### Componentes de UI personalizados

| Estilo | Base Material3 | Personalización |
|---|---|---|
| `Widget.TomenAguita.Button` | `Widget.Material3.Button` | Alto 48dp, esquinas 12dp, sin mayúsculas |
| `Widget.TomenAguita.Button.Outlined` | `Widget.Material3.Button.OutlinedButton` | Alto 48dp, esquinas 12dp |
| `Widget.TomenAguita.TextInputLayout` | `Widget.Material3.TextInputLayout.OutlinedBox` | Esquinas 8dp, borde primario |

---

## Bibliotecas utilizadas

### AndroidX y UI
| Biblioteca | Versión | Propósito |
|---|---|---|
| AndroidX Core KTX | 1.12.0 | Extensiones Kotlin para Android |
| AppCompat | 1.6.1 | Compatibilidad hacia atrás |
| Material Components | 1.11.0 | Material Design 3 (botones, chips, tarjetas…) |
| ConstraintLayout | 2.1.4 | Layouts de alta complejidad |
| RecyclerView | 1.3.2 | Listas de productos, pedidos y usuarios |
| CardView | 1.0.0 | Tarjetas de productos |
| ViewPager2 | 1.0.0 | Carrusel en pantalla de inicio |
| Navigation Fragment KTX | 2.7.7 | Navegación entre fragmentos |
| Navigation UI KTX | 2.7.7 | Integración con BottomNav y Drawer |
| Navigation SafeArgs | 2.7.7 | Paso de argumentos tipados |
| SwipeRefreshLayout | 1.1.0 | Pull-to-refresh |
| CoordinatorLayout | 1.2.0 | Comportamientos coordinados |

### Datos y persistencia
| Biblioteca | Versión | Propósito |
|---|---|---|
| Room Runtime | 2.6.1 | ORM sobre SQLite (base de datos local) |
| Room KTX | 2.6.1 | Soporte de corrutinas en Room |
| Room Compiler (KAPT) | 2.6.1 | Generación de código en compilación |
| Lifecycle ViewModel KTX | 2.7.0 | ViewModel con soporte de corrutinas |
| Lifecycle LiveData KTX | 2.7.0 | LiveData + Flow.asLiveData() |

### Firebase
| Biblioteca | Versión | Propósito |
|---|---|---|
| Firebase BOM | 32.7.0 | Gestión de versiones de Firebase |
| Firebase Auth KTX | (BOM) | Autenticación de usuarios |
| Firebase Firestore KTX | (BOM) | Base de datos en la nube |
| Firebase Storage KTX | (BOM) | Almacenamiento de imágenes |

### Pagos y mapas
| Biblioteca | Versión | Propósito |
|---|---|---|
| Stripe Android | 20.36.0 | PaymentSheet para procesar tarjetas |
| Play Services Location | 21.1.0 | GPS y FusedLocationProvider |
| Play Services Maps | 18.2.0 | Google Maps SDK |

### Seguridad y utilidades
| Biblioteca | Versión | Propósito |
|---|---|---|
| Security Crypto | 1.1.0-alpha06 | EncryptedSharedPreferences |
| Biometric | 1.1.0 | Autenticación por huella digital |
| Glide | 4.16.0 | Carga y caché de imágenes |

### Sistema de construcción
| Herramienta | Versión |
|---|---|
| Android Gradle Plugin | 8.2.0 |
| Kotlin | 1.9.22 |
| Gradle Wrapper | 8.2 |

---

## Permisos del dispositivo

| Permiso | Cuándo se usa |
|---|---|
| `INTERNET` | Firebase, Stripe API, Google Maps |
| `CAMERA` | Foto de perfil del usuario |
| `READ_EXTERNAL_STORAGE` | Galería de imágenes (Android ≤ 12) |
| `READ_MEDIA_IMAGES` | Galería de imágenes (Android 13+) |
| `ACCESS_FINE_LOCATION` | GPS para dirección de entrega en mapa |
| `ACCESS_COARSE_LOCATION` | Ubicación aproximada como respaldo |
| `USE_BIOMETRIC` | Huella digital en pantalla de login |

> La cámara es marcada como `android:required="false"` — la app funciona en dispositivos sin cámara.

---

## Credenciales de demostración

### Usuarios de prueba

Los usuarios se crean en Firebase Auth. Para desarrollo local, registra manualmente las cuentas con los siguientes correos o usa la consola de Firebase.

| Rol | Correo sugerido | Contraseña sugerida |
|---|---|---|
| Administrador | `admin@tomenaguita.com` | `Admin123!` |
| Vendedor | `vendedor@tomenaguita.com` | `Vendedor123!` |
| Comprador | `comprador@tomenaguita.com` | `Comprador123!` |

> El rol se asigna en el documento del usuario en Firestore (colección `usuarios`, campo `rol`).

### Pagos Stripe sandbox

| Descripción | Valor |
|---|---|
| Publishable Key | `pk_test_51TZfJQ...` (ver `Constants.kt`) |
| Secret Key | `sk_test_51TZfJQ...` (ver `Constants.kt`) |
| Visa aprobada | `4242 4242 4242 4242` |
| Visa declinada | `4000 0000 0000 0002` |
| Fecha | Cualquier fecha futura |
| CVV | Cualquier 3 dígitos |

### Productos de demostración

La app pre-puebla 8 productos en Firestore en la primera ejecución:

| Producto | Presentación | Precio COP |
|---|---|---|
| Botella personal | 300 ml | $1.500 |
| Botella mediana | 500 ml | $2.500 |
| Botella familiar | 1 litro | $4.000 |
| Botellón | 5 litros | $12.000 |
| Pack personal | 24 × 300 ml | $30.000 |
| Pack mediano | 12 × 500 ml | $25.000 |
| Pack familiar | 6 × 1 litro | $20.000 |
| Garrafón | 20 litros | $18.000 |

---

## Instalación y configuración

### Requisitos previos
- Android Studio Hedgehog (2023.1.1) o superior
- JDK 17
- Android SDK con API 24–34 instalado
- Proyecto Firebase configurado con Auth, Firestore y Storage habilitados
- API Key de Google Maps con Android Maps SDK activado
- Cuenta Stripe con claves sandbox

### Pasos de instalación

```bash
# 1. Clonar el repositorio
git clone https://github.com/gegitrepo/TomenAguitaTienda.git

# 2. Abrir en Android Studio
#    File → Open → seleccionar la carpeta del proyecto

# 3. Agregar google-services.json
#    Descargarlo de Firebase Console → Project Settings → Your apps
#    Copiarlo en app/

# 4. Verificar local.properties
#    Android Studio lo genera automáticamente con la ruta del SDK:
#    sdk.dir=C\:\\Users\\TuUsuario\\AppData\\Local\\Android\\Sdk

# 5. Verificar gradle.properties
#    Debe contener:
#    android.useAndroidX=true
#    android.enableJetifier=true

# 6. Sincronizar Gradle y compilar
#    Build → Make Project

# 7. Ejecutar en dispositivo o emulador (API 24+)
#    Run → Run 'app'
```

### Variables de entorno / credenciales

Todas las credenciales están en `app/src/main/java/com/example/tomenaguita/utils/Constants.kt`:

```kotlin
const val STRIPE_PUBLISHABLE_KEY = "pk_test_..."  // Reemplazar con tu clave
const val STRIPE_SECRET_KEY      = "sk_test_..."  // Reemplazar con tu clave
```

La API Key de Google Maps está en `AndroidManifest.xml`:
```xml
<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="AIzaSy..." />
```

> Para producción, mover estas credenciales a un servidor backend (Stripe) y a `local.properties` (Google Maps).

---

## Información académica

| Campo | Detalle |
|---|---|
| Institución | Fundación Universitaria Compensar |
| Programa | Desarrollo de Aplicaciones Móviles |
| Autor | Gonzalo E. González |
| Contacto | gegonzalez.1208@gmail.com |
| Fecha | Mayo 2026 |

### Aviso legal
Este proyecto es de carácter **exclusivamente académico**. No constituye un servicio comercial real. Los datos de usuarios, productos y pedidos son completamente ficticios. Las transacciones de pago se realizan en entorno **sandbox** de Stripe y no generan cobros reales. Las marcas, nombres y precios son inventados para fines ilustrativos.

---

*Desarrollado con Kotlin · Android Studio · Firebase · Stripe · Google Maps · Material Design 3*
