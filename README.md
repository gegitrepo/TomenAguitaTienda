# Tomen Agüita — Tienda de Agua Embotellada

> **Proyecto académico** — Fundación Universitaria Compensar · Desarrollo de Aplicaciones Móviles
>
> Esta aplicación fue desarrollada con fines estrictamente educativos como parte de una actividad académica.
> **No constituye una tienda real**, no procesa pagos reales y todos sus datos son simulados.

---

## Tabla de contenidos

1. [Descripción general](#descripción-general)
2. [Finalidad del proyecto](#finalidad-del-proyecto)
3. [Características principales](#características-principales)
4. [Roles de usuario](#roles-de-usuario)
5. [Arquitectura del proyecto](#arquitectura-del-proyecto)
6. [Jerarquía de archivos](#jerarquía-de-archivos)
7. [Diseño y paleta de colores](#diseño-y-paleta-de-colores)
8. [Bibliotecas utilizadas](#bibliotecas-utilizadas)
9. [Permisos del dispositivo](#permisos-del-dispositivo)
10. [Credenciales de demostración](#credenciales-de-demostración)
11. [Instalación y configuración](#instalación-y-configuración)
12. [Información académica](#información-académica)

---

## Descripción general

**Tomen Agüita** es una aplicación Android para la gestión de una tienda de agua purificada embotellada en Colombia. Permite a compradores explorar y simular la compra de productos, a vendedores gestionar su catálogo y pedidos, y a administradores supervisar usuarios, productos y reportes de ventas.

La app está desarrollada en **Kotlin** con arquitectura **MVVM**, base de datos local **Room**, navegación con **Jetpack Navigation Component + SafeArgs**, y sigue las guías de diseño de **Material Design 3**.

| Dato | Valor |
|---|---|
| Plataforma | Android |
| Lenguaje | Kotlin |
| SDK mínimo | API 24 (Android 7.0 Nougat) |
| SDK objetivo | API 34 (Android 14) |
| Arquitectura | MVVM (Model – View – ViewModel) |
| ID de paquete | `com.example.tomenaguita` |

---

## Finalidad del proyecto

El objetivo académico de la aplicación es demostrar el dominio de los siguientes conceptos del desarrollo de aplicaciones móviles nativas para Android:

- Diseño de interfaces con **XML Views** y **Material Design 3**
- Implementación del patrón **MVVM** con `ViewModel`, `LiveData` y `Flow`
- Gestión de base de datos local con **Room / SQLite**
- Navegación entre pantallas con **Jetpack Navigation Component** y **SafeArgs**
- Manejo de sesión segura con **EncryptedSharedPreferences**
- Autenticación biométrica con **BiometricPrompt**
- Arquitectura por capas: datos, dominio y presentación
- Control de acceso por **roles de usuario**

> En su estado actual (**Actividad 3 — Capa de presentación**), todos los datos mostrados son simulados y no se realizan operaciones reales sobre la base de datos. La infraestructura de Room, DAOs y repositorios está implementada y lista para ser conectada en una actividad futura.

---

## Características principales

### Autenticación
- Pantalla de inicio de sesión con validación de campos
- Registro de nuevos usuarios con validación de correo y teléfono colombiano
- Recuperación de contraseña (flujo de UI)
- Autenticación biométrica con huella digital (si el dispositivo lo soporta)
- Sesión persistente cifrada con `EncryptedSharedPreferences`

### Comprador
- Catálogo de productos con búsqueda
- Vista detallada de cada producto con selección de cantidad
- Carrito de compras con control de cantidades
- Resumen del pedido con dirección de entrega
- Pasarela de pago simulada (efectivo / tarjeta)
- Historial de pedidos con filtros por estado
- Detalle de cada pedido
- Perfil de usuario con foto y datos editables

### Vendedor
- Listado de mis productos con estado de disponibilidad
- Crear y editar productos del catálogo
- Gestión de pedidos recibidos con avance de estado
- Detalle del pedido con información del comprador
- Perfil del vendedor

### Administrador
- Dashboard con métricas generales (usuarios, productos, pedidos, ventas)
- Gestión completa de usuarios (crear, editar, desactivar — soft delete)
- Gestión de productos del catálogo
- Reporte de ventas

### Generales
- Splash screen con animación de fade-in
- Enrutamiento automático por rol al iniciar sesión
- Navegación hacia atrás consistente en todas las pantallas
- Numeración de pedidos en formato `TA-YYYYMMDD-XXXX`
- Formato de precios en pesos colombianos (COP)

---

## Roles de usuario

La aplicación define tres roles con interfaces completamente independientes:

### Comprador (`COMPRADOR`)
- **Navegación:** `BottomNavigationView` con 5 pestañas
- **Pantallas:** Inicio · Catálogo · Carrito · Mis Pedidos · Perfil
- **Acceso:** Explorar productos, agregar al carrito, realizar pedidos, ver historial

### Vendedor (`VENDEDOR`)
- **Navegación:** `BottomNavigationView` con 3 pestañas
- **Pantallas:** Mis Productos · Pedidos Recibidos · Perfil
- **Acceso:** Gestionar su propio catálogo, ver y avanzar el estado de los pedidos

### Administrador (`ADMINISTRADOR`)
- **Navegación:** `NavigationDrawer` (menú lateral) con Toolbar
- **Pantallas:** Dashboard · Usuarios · Productos · Reportes
- **Acceso:** Supervisión global de la plataforma, gestión de usuarios y productos

Cada rol tiene su propia `Activity` principal y su propio grafo de navegación (`NavGraph`). El enrutamiento se realiza automáticamente en el `SplashActivity` según el rol guardado en sesión.

---

## Arquitectura del proyecto

La aplicación sigue el patrón **MVVM** recomendado por Google con separación en tres capas:

```
┌─────────────────────────────────────────┐
│              UI Layer                   │
│  Activities · Fragments · Adapters      │
│  (observan LiveData del ViewModel)      │
├─────────────────────────────────────────┤
│           ViewModel Layer               │
│  AndroidViewModel · LiveData · Flow     │
│  (expone estado, maneja lógica de UI)   │
├─────────────────────────────────────────┤
│            Data Layer                   │
│  Repository → DAO → Room Database       │
│  (fuente única de verdad)               │
└─────────────────────────────────────────┘
```

### Flujo de datos
```
Fragment → ViewModel → Repository → DAO → Room DB
                ↑                              |
           LiveData  ←──────────── Flow.asLiveData()
```

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
│   │   │   └── AppDatabase.kt          ← singleton Room
│   │   ├── model/
│   │   │   ├── EstadoPedido.kt         ← enum con transiciones de estado
│   │   │   └── Rol.kt                  ← enum COMPRADOR / VENDEDOR / ADMINISTRADOR
│   │   └── repository/
│   │       ├── CarritoRepository.kt
│   │       ├── PedidoRepository.kt
│   │       ├── ProductoRepository.kt
│   │       └── UsuarioRepository.kt
│   │
│   ├── ui/
│   │   ├── adapter/                    ← adaptadores compartidos entre roles
│   │   │   ├── CarritoAdapter.kt
│   │   │   ├── PedidoAdapter.kt
│   │   │   ├── ProductoAdapter.kt
│   │   │   └── UsuarioAdapter.kt
│   │   ├── admin/
│   │   │   ├── AdminMainActivity.kt    ← DrawerLayout + Toolbar
│   │   │   ├── dashboard/DashboardFragment.kt
│   │   │   ├── productos/GestionProductosFragment.kt
│   │   │   ├── reportes/ReporteVentasFragment.kt
│   │   │   └── usuarios/
│   │   │       ├── ListaUsuariosFragment.kt
│   │   │       ├── CrearUsuarioFragment.kt
│   │   │       └── EditarUsuarioFragment.kt
│   │   ├── auth/
│   │   │   ├── LoginActivity.kt
│   │   │   ├── RegisterActivity.kt
│   │   │   └── ForgotPasswordActivity.kt
│   │   ├── comprador/
│   │   │   ├── CompradorMainActivity.kt  ← BottomNavigationView (5 tabs)
│   │   │   ├── carrito/CarritoFragment.kt
│   │   │   ├── catalogo/CatalogoFragment.kt
│   │   │   ├── home/HomeFragment.kt
│   │   │   ├── pago/
│   │   │   │   ├── ResumenPedidoFragment.kt
│   │   │   │   └── PasarelaPagoFragment.kt
│   │   │   ├── pedidos/
│   │   │   │   ├── HistorialPedidosFragment.kt
│   │   │   │   └── DetallePedidoFragment.kt
│   │   │   ├── perfil/
│   │   │   │   ├── PerfilFragment.kt
│   │   │   │   └── EditarPerfilFragment.kt
│   │   │   └── producto/ProductoDetalleFragment.kt
│   │   ├── splash/SplashActivity.kt
│   │   └── vendedor/
│   │       ├── VendedorMainActivity.kt   ← BottomNavigationView (3 tabs)
│   │       ├── pedidos/
│   │       │   ├── PedidosRecibidosFragment.kt
│   │       │   └── DetallePedidoVendedorFragment.kt
│   │       ├── perfil/PerfilVendedorFragment.kt
│   │       └── productos/
│   │           ├── MisProductosFragment.kt
│   │           └── CrearEditarProductoFragment.kt
│   │
│   ├── utils/
│   │   ├── BiometricHelper.kt          ← wrapper de BiometricPrompt
│   │   ├── Constants.kt                ← keys, datos demo, presentaciones
│   │   ├── Extensions.kt               ← toCOP(), showSnackbar(), isValidEmail()…
│   │   └── SessionManager.kt           ← EncryptedSharedPreferences
│   │
│   └── viewmodel/                      ← ViewModels compartidos entre roles
│       ├── AuthViewModel.kt
│       ├── CarritoViewModel.kt
│       ├── PedidoViewModel.kt
│       ├── ProductoViewModel.kt
│       └── UsuarioViewModel.kt
│
└── res/
    ├── drawable/       ← íconos SVG, fondos, selectores
    ├── layout/         ← 7 activities + 20 fragments + 4 items + nav_header
    ├── menu/           ← bottom_nav_comprador, bottom_nav_vendedor, drawer_admin
    ├── mipmap-anydpi/          ← ícono de lanzador (API 24–25, vector)
    ├── mipmap-anydpi-v26/      ← ícono adaptativo (API 26+)
    ├── navigation/     ← nav_graph_comprador, nav_graph_vendedor, nav_graph_admin
    └── values/
        ├── colors.xml
        ├── dimens.xml
        ├── strings.xml
        └── themes.xml
```

---

## Diseño y paleta de colores

El diseño sigue las guías de **Material Design 3** con el tema base `Theme.Material3.DayNight.NoActionBar`.

### Paleta principal

| Rol | Nombre | Hex | Uso |
|---|---|---|---|
| Primario | `primary` | `#1565C0` | Botones principales, AppBar, elementos activos |
| Primario oscuro | `primary_dark` | `#0D47A1` | StatusBar, fondo splash |
| Primario claro | `primary_light` | `#42A5F5` | Highlights, iconos secundarios |
| Secundario | `secondary` | `#00BCD4` | Acento, gradiente splash |
| Secundario oscuro | `secondary_dark` | `#00838F` | Hover en elementos secundarios |
| Acento | `accent` | `#26A69A` | Chips, indicadores |

### Paleta de superficie y fondo

| Nombre | Hex | Uso |
|---|---|---|
| `background` | `#FAFAFA` | Fondo general de pantallas |
| `surface` | `#FFFFFF` | Tarjetas, diálogos, BottomBar |
| `on_primary` | `#FFFFFF` | Texto sobre elementos primarios |
| `on_background` | `#212121` | Texto principal |
| `on_background_secondary` | `#757575` | Texto secundario / subtítulos |
| `divider` | `#BDBDBD` | Separadores |
| `input_background` | `#F5F9FF` | Fondo de campos de texto |

### Paleta de estados de pedido

| Estado | Color | Hex |
|---|---|---|
| Pendiente | Naranja | `#FFA500` |
| Pagado | Azul | `#2196F3` |
| Enviado | Morado | `#9C27B0` |
| Entregado | Verde | `#4CAF50` |
| Cancelado | Rojo | `#F44336` |

### Gradiente splash
El splash screen usa un gradiente diagonal (135°) de `#0D47A1` → `#00BCD4`.

### Tipografía
- Fuente del sistema (`sans-serif`) en todos los tamaños
- Escala: `text_small` 12sp · `text_body` 14sp · `text_medium` 16sp · `text_large` 18sp · `text_title` 22sp · `text_headline` 28sp
- Precios en negrita con color `primary`

### Componentes de UI personalizados
| Estilo | Base Material3 | Personalización |
|---|---|---|
| `Widget.TomenAguita.Button` | `Widget.Material3.Button` | Alto 48dp, esquinas 12dp, sin mayúsculas |
| `Widget.TomenAguita.Button.Outlined` | `Widget.Material3.Button.OutlinedButton` | Alto 48dp, esquinas 12dp |
| `Widget.TomenAguita.TextInputLayout` | `Widget.Material3.TextInputLayout.OutlinedBox` | Esquinas 8dp, borde primario |

---

## Bibliotecas utilizadas

| Biblioteca | Versión | Propósito |
|---|---|---|
| **AndroidX Core KTX** | 1.12.0 | Extensiones Kotlin para Android |
| **AppCompat** | 1.6.1 | Compatibilidad hacia atrás |
| **Material Components** | 1.11.0 | Material Design 3 (botones, campos, chips, etc.) |
| **ConstraintLayout** | 2.1.4 | Layouts de alta complejidad |
| **RecyclerView** | 1.3.2 | Listas de productos, pedidos y usuarios |
| **CardView** | 1.0.0 | Tarjetas de productos |
| **ViewPager2** | 1.0.0 | Banners / carrusel en pantalla de inicio |
| **Navigation Fragment KTX** | 2.7.7 | Navegación entre fragmentos con back stack |
| **Navigation UI KTX** | 2.7.7 | Integración con BottomNav y Drawer |
| **Navigation SafeArgs** | 2.7.7 | Paso de argumentos tipados entre destinos |
| **Room Runtime** | 2.6.1 | ORM sobre SQLite — entidades y queries |
| **Room KTX** | 2.6.1 | Soporte de corrutinas en Room |
| **Room Compiler (KAPT)** | 2.6.1 | Generación de código en tiempo de compilación |
| **Lifecycle ViewModel KTX** | 2.7.0 | ViewModel con soporte de corrutinas |
| **Lifecycle LiveData KTX** | 2.7.0 | LiveData + Flow → asLiveData() |
| **Biometric** | 1.1.0 | Autenticación por huella digital |
| **Glide** | 4.16.0 | Carga y caché de imágenes |
| **Play Services Location** | 21.1.0 | Obtención de ubicación GPS |
| **Play Services Maps** | 18.2.0 | Integración con Google Maps |
| **Security Crypto** | 1.1.0-alpha06 | `EncryptedSharedPreferences` para sesión segura |
| **SwipeRefreshLayout** | 1.1.0 | Pull-to-refresh en listas |
| **CoordinatorLayout** | 1.2.0 | Comportamientos coordinados (FAB + AppBar) |

### Sistema de construcción
| Herramienta | Versión |
|---|---|
| Android Gradle Plugin | 8.2.0 |
| Kotlin | 1.9.22 |
| Gradle | 8.2 |
| KAPT | generación de código Room + SafeArgs |

---

## Permisos del dispositivo

| Permiso | Cuándo se usa |
|---|---|
| `INTERNET` | Carga de imágenes remotas con Glide, futura integración con API |
| `CAMERA` | Foto de perfil del usuario (cámara del dispositivo) |
| `READ_EXTERNAL_STORAGE` | Selección de imagen de galería (Android ≤ 12) |
| `READ_MEDIA_IMAGES` | Selección de imagen de galería (Android 13+) |
| `ACCESS_FINE_LOCATION` | Ubicación precisa GPS para dirección de entrega |
| `ACCESS_COARSE_LOCATION` | Ubicación aproximada como respaldo |
| `USE_BIOMETRIC` | Autenticación con huella digital en la pantalla de login |

> La cámara es marcada como `android:required="false"` — la app funciona en dispositivos sin cámara.

---

## Credenciales de demostración

Dado que la aplicación usa datos simulados, las credenciales de acceso están hardcodeadas. La contraseña no es validada en Actividad 3 — solo el correo determina el rol.

| Rol | Correo | Contraseña |
|---|---|---|
| Administrador | `admin@tomenaguita.com` | `Admin123!` |
| Vendedor | `vendedor@tomenaguita.com` | `Vendedor123!` |
| Comprador | `comprador@tomenaguita.com` | `Comprador123!` |

### Productos de demostración
La tienda incluye 8 productos de muestra:

| Producto | Presentación | Precio |
|---|---|---|
| Botella personal | 300 ml | $1.500 COP |
| Botella mediana | 500 ml | $2.500 COP |
| Botella familiar | 1 litro | $4.000 COP |
| Botellón | 5 litros | $12.000 COP |
| Pack personal | 24 × 300 ml | $30.000 COP |
| Pack mediano | 12 × 500 ml | $25.000 COP |
| Pack familiar | 6 × 1 litro | $20.000 COP |
| Garrafón | 20 litros | $18.000 COP |

---

## Instalación y configuración

### Requisitos
- Android Studio Hedgehog (2023.1.1) o superior
- JDK 17
- Android SDK con API 24–34 instalado
- Dispositivo o emulador con Android 7.0+

### Pasos
```bash
# 1. Clonar el repositorio
git clone https://github.com/gegitrepo/TomenAguitaTienda.git

# 2. Abrir en Android Studio
#    File → Open → seleccionar la carpeta clonada

# 3. Esperar el Gradle sync (descarga ~200 MB de dependencias)

# 4. Ejecutar en dispositivo o emulador (API 24+)
#    Run → Run 'app'
```

> El primer build puede tardar 3–5 minutos mientras Gradle descarga dependencias y SafeArgs genera las clases de navegación.

---

## Información académica

| Campo | Detalle |
|---|---|
| Institución | Fundación Universitaria Compensar |
| Programa | Desarrollo de Aplicaciones Móviles |
| Actividad | Actividad 3 — Capa de presentación (UI) |
| Autor | Gonzalo E. González |
| Contacto | gegonzalez.1208@gmail.com |

### Aviso legal
Este proyecto es de carácter **exclusivamente académico**. No constituye un servicio comercial real. Los datos de usuarios, productos, pedidos y transacciones son completamente **ficticios y simulados**. No se realizan cobros ni transacciones financieras reales. Las marcas, nombres y precios mencionados son inventados para fines ilustrativos.

---

*Desarrollado con Kotlin · Android Studio · Material Design 3*
