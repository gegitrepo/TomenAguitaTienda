# TomenAguita — Tienda Virtual de Agua Purificada

## Descripción del Proyecto

TomenAguita es una aplicación móvil nativa Android para la venta de agua purificada en Colombia. Es un proyecto académico de la Fundación Universitaria Compensar desarrollado por Gonzalo E. González. La app opera como una tienda virtual con tres roles de usuario (Comprador, Vendedor, Administrador) y un catálogo de 8 presentaciones de agua.

---

## Stack Tecnológico

| Aspecto | Tecnología |
|---|---|
| Lenguaje | Kotlin |
| IDE | Android Studio |
| Min SDK | API 24 (Android 7.0 Nougat) |
| Target SDK | 34 (Android 14) |
| UI Framework | Android Views/XML (NO Compose) |
| Arquitectura | MVVM (Model-View-ViewModel) |
| Base de datos | SQLite con Room |
| Navegación | Jetpack Navigation Component (NavGraph + SafeArgs) |
| Binding | ViewBinding |
| Inyección de dependencias | Manual (o Hilt si se requiere) |
| Async | Kotlin Coroutines + Flow |

---

## Paleta de Colores (extraer de mockups Figma)

Los colores deben extraerse del proyecto de Figma. Referencia general basada en la temática de agua purificada:

```xml
<!-- res/values/colors.xml -->
<!-- IMPORTANTE: Reemplazar estos valores con los colores EXACTOS de los mockups de Figma -->
<resources>
    <color name="primary">#1565C0</color>           <!-- Azul principal -->
    <color name="primary_dark">#0D47A1</color>       <!-- Azul oscuro -->
    <color name="primary_light">#42A5F5</color>      <!-- Azul claro -->
    <color name="secondary">#00BCD4</color>          <!-- Cyan/turquesa -->
    <color name="secondary_dark">#00838F</color>     <!-- Cyan oscuro -->
    <color name="accent">#26A69A</color>             <!-- Teal accent -->
    <color name="background">#FAFAFA</color>         <!-- Fondo claro -->
    <color name="surface">#FFFFFF</color>            <!-- Superficie -->
    <color name="error">#B00020</color>              <!-- Rojo error -->
    <color name="success">#4CAF50</color>            <!-- Verde éxito -->
    <color name="warning">#FFA000</color>            <!-- Naranja advertencia -->
    <color name="on_primary">#FFFFFF</color>         <!-- Texto sobre primary -->
    <color name="on_background">#212121</color>      <!-- Texto principal -->
    <color name="on_background_secondary">#757575</color> <!-- Texto secundario -->
    <color name="divider">#BDBDBD</color>            <!-- Líneas divisoras -->

    <!-- Colores de estado de pedidos -->
    <color name="status_pending">#FFA500</color>     <!-- Pendiente: naranja -->
    <color name="status_paid">#2196F3</color>        <!-- Pagado: azul -->
    <color name="status_shipped">#9C27B0</color>     <!-- Enviado: morado -->
    <color name="status_delivered">#4CAF50</color>   <!-- Entregado: verde -->
    <color name="status_cancelled">#F44336</color>   <!-- Cancelado: rojo -->
</resources>
```

**Acción requerida:** El desarrollador debe acceder al proyecto de Figma de TomenAguita, extraer los colores exactos de los mockups y actualizar `colors.xml`. Si se tiene acceso al MCP de Figma, usar `get_design_context` o `get_variable_defs` para obtener los tokens de color.

---

## Estructura de Paquetes

```
com.example.tomenaguita/
├── data/
│   ├── database/
│   │   ├── AppDatabase.kt              // Room database singleton
│   │   ├── dao/
│   │   │   ├── UsuarioDao.kt
│   │   │   ├── ProductoDao.kt
│   │   │   ├── CarritoDao.kt
│   │   │   └── PedidoDao.kt
│   │   └── entity/
│   │       ├── Usuario.kt              // @Entity
│   │       ├── Producto.kt             // @Entity
│   │       ├── CarritoItem.kt          // @Entity
│   │       ├── Pedido.kt               // @Entity
│   │       └── DetallePedido.kt        // @Entity
│   ├── repository/
│   │   ├── UsuarioRepository.kt
│   │   ├── ProductoRepository.kt
│   │   ├── CarritoRepository.kt
│   │   └── PedidoRepository.kt
│   └── model/
│       └── enums/
│           ├── Rol.kt                  // enum: COMPRADOR, VENDEDOR, ADMINISTRADOR
│           └── EstadoPedido.kt         // enum: PENDIENTE, PAGADO, ENVIADO, ENTREGADO, CANCELADO
├── ui/
│   ├── splash/
│   │   └── SplashActivity.kt
│   ├── auth/
│   │   ├── LoginActivity.kt
│   │   ├── RegisterActivity.kt
│   │   └── ForgotPasswordActivity.kt
│   ├── comprador/
│   │   ├── CompradorMainActivity.kt    // Host con BottomNavigationView
│   │   ├── home/
│   │   │   └── HomeFragment.kt         // Catálogo de productos
│   │   ├── producto/
│   │   │   └── ProductoDetalleFragment.kt
│   │   ├── carrito/
│   │   │   └── CarritoFragment.kt
│   │   ├── pago/
│   │   │   ├── ResumenPedidoFragment.kt
│   │   │   └── PasarelaPagoFragment.kt
│   │   ├── pedidos/
│   │   │   ├── HistorialPedidosFragment.kt
│   │   │   └── DetallePedidoFragment.kt
│   │   └── perfil/
│   │       ├── PerfilFragment.kt
│   │       └── EditarPerfilFragment.kt
│   ├── vendedor/
│   │   ├── VendedorMainActivity.kt     // Host con BottomNavigationView
│   │   ├── productos/
│   │   │   ├── MisProductosFragment.kt
│   │   │   └── CrearEditarProductoFragment.kt
│   │   ├── pedidos/
│   │   │   ├── PedidosRecibidosFragment.kt
│   │   │   └── DetallePedidoVendedorFragment.kt
│   │   └── perfil/
│   │       └── PerfilVendedorFragment.kt
│   ├── admin/
│   │   ├── AdminMainActivity.kt        // Host con NavigationDrawer
│   │   ├── dashboard/
│   │   │   └── DashboardFragment.kt
│   │   ├── usuarios/
│   │   │   ├── ListaUsuariosFragment.kt
│   │   │   ├── CrearUsuarioFragment.kt
│   │   │   └── EditarUsuarioFragment.kt
│   │   ├── productos/
│   │   │   └── GestionProductosFragment.kt
│   │   └── reportes/
│   │       └── ReporteVentasFragment.kt
│   └── adapter/
│       ├── ProductoAdapter.kt          // RecyclerView adapter para productos
│       ├── CarritoAdapter.kt           // RecyclerView adapter para carrito
│       ├── PedidoAdapter.kt            // RecyclerView adapter para pedidos
│       └── UsuarioAdapter.kt           // RecyclerView adapter para usuarios (admin)
├── utils/
│   ├── SessionManager.kt              // Manejo de sesión con SharedPreferences cifradas
│   ├── BiometricHelper.kt             // Wrapper para BiometricPrompt
│   ├── Constants.kt                   // Constantes de la app
│   └── Extensions.kt                  // Funciones de extensión Kotlin
└── viewmodel/
    ├── AuthViewModel.kt
    ├── ProductoViewModel.kt
    ├── CarritoViewModel.kt
    ├── PedidoViewModel.kt
    └── UsuarioViewModel.kt
```

---

## Pantallas Requeridas por Rol

### Transversales (Todos los roles)

| # | Pantalla | Activity/Fragment | Layout XML | Componentes Principales |
|---|---|---|---|---|
| T-01 | Splash Screen | SplashActivity | activity_splash.xml | ImageView (logo), ProgressBar, animación fade-in |
| T-02 | Login | LoginActivity | activity_login.xml | TextInputLayout (email, password), Button login, Button biometría (fingerprint icon), links a registro y recuperar contraseña |
| T-03 | Registro | RegisterActivity | activity_register.xml | TextInputLayout (nombre, email, teléfono, password, confirmar password), CheckBox términos, Button registrarse |
| T-04 | Recuperar Contraseña | ForgotPasswordActivity | activity_forgot_password.xml | TextInputLayout (email), Button enviar enlace, texto informativo |

### Comprador (BottomNavigationView con 5 ítems: Inicio, Catálogo, Carrito, Pedidos, Perfil)

| # | Pantalla | Fragment | Layout XML | Componentes Principales |
|---|---|---|---|---|
| C-01 | Inicio / Home | HomeFragment | fragment_home.xml | ViewPager2 (banners), RecyclerView (productos destacados), SearchView, categorías |
| C-02 | Detalle de Producto | ProductoDetalleFragment | fragment_producto_detalle.xml | ImageView (foto producto), TextView (nombre, descripción, presentación, precio), NumberPicker (cantidad), Button "Agregar al carrito" |
| C-03 | Carrito de Compras | CarritoFragment | fragment_carrito.xml | RecyclerView (ítems del carrito con +/- cantidad y eliminar), TextView total, Button "Proceder al pago", Button "Vaciar carrito" |
| C-04 | Resumen del Pedido | ResumenPedidoFragment | fragment_resumen_pedido.xml | RecyclerView (resumen de productos, solo lectura), TextViews (subtotal, envío, total), dirección de entrega, Button "Confirmar y pagar" |
| C-05 | Pasarela de Pagos | PasarelaPagoFragment | fragment_pasarela_pago.xml | Selección método de pago (RadioButtons), datos de pago (simulado), Button "Pagar" |
| C-06 | Historial de Pedidos | HistorialPedidosFragment | fragment_historial_pedidos.xml | RecyclerView (pedidos con Chip de estado color-coded), filtros por estado |
| C-07 | Detalle de Pedido | DetallePedidoFragment | fragment_detalle_pedido.xml | Timeline de estados, lista de productos, totales, dirección, información de seguimiento |
| C-08 | Perfil | PerfilFragment | fragment_perfil.xml | CircleImageView (foto), TextViews (datos personales), Button "Editar", mapa con dirección |
| C-09 | Editar Perfil | EditarPerfilFragment | fragment_editar_perfil.xml | TextInputLayouts editables, ImageView (cambiar foto), Button cámara/galería, mapa para ubicación |

### Vendedor (BottomNavigationView con 3 ítems: Productos, Pedidos, Perfil)

| # | Pantalla | Fragment | Layout XML | Componentes Principales |
|---|---|---|---|---|
| V-01 | Mis Productos | MisProductosFragment | fragment_mis_productos.xml | RecyclerView (CardView con imagen, nombre, precio, stock, disponibilidad), FAB "+" para crear, SwipeRefreshLayout |
| V-02 | Crear/Editar Producto | CrearEditarProductoFragment | fragment_crear_editar_producto.xml | ImageView (foto), Button cámara/galería, TextInputLayouts (nombre, descripción, precio, stock), Spinner (presentación), Switch (disponible), Button guardar |
| V-03 | Pedidos Recibidos | PedidosRecibidosFragment | fragment_pedidos_recibidos.xml | RecyclerView (pedidos agrupados por estado), Chip filters por estado, badge de nuevos pedidos |
| V-04 | Detalle de Pedido | DetallePedidoVendedorFragment | fragment_detalle_pedido_vendedor.xml | Datos del comprador (nombre, teléfono), lista de productos, total, dirección, Button "Marcar como enviado/entregado" |
| V-05 | Perfil Vendedor | PerfilVendedorFragment | fragment_perfil_vendedor.xml | Misma estructura que perfil comprador, con datos del vendedor |

### Administrador (NavigationDrawer con: Dashboard, Usuarios, Productos, Reportes, Perfil)

| # | Pantalla | Fragment | Layout XML | Componentes Principales |
|---|---|---|---|---|
| A-01 | Dashboard | DashboardFragment | fragment_dashboard.xml | CardViews con métricas (total usuarios, productos, pedidos, ventas), accesos rápidos |
| A-02 | Lista de Usuarios | ListaUsuariosFragment | fragment_lista_usuarios.xml | SearchView, RecyclerView (nombre, email, rol con Chip, estado activo/inactivo), FAB crear usuario |
| A-03 | Crear Usuario | CrearUsuarioFragment | fragment_crear_usuario.xml | TextInputLayouts (nombre, email, teléfono, password), Spinner (rol), Switch (activo), Button crear |
| A-04 | Editar Usuario | EditarUsuarioFragment | fragment_editar_usuario.xml | Mismos campos que crear prellenados, Spinner rol, Switch activo, Buttons guardar/eliminar |
| A-05 | Gestión de Productos | GestionProductosFragment | fragment_gestion_productos.xml | RecyclerView de todos los productos (todos los vendedores), filtros, acciones de editar/eliminar |
| A-06 | Reporte de Ventas | ReporteVentasFragment | fragment_reporte_ventas.xml | CardViews (totales), RecyclerView (últimas ventas), filtros por fecha |

**Total: ~24 pantallas** (4 transversales + 9 comprador + 5 vendedor + 6 administrador)

---

## Modelo de Datos (Room / SQLite)

### Tabla: usuarios
| Campo | Tipo | Restricciones | Descripción |
|---|---|---|---|
| id | INTEGER | PK, AUTOINCREMENT | ID único |
| nombre | TEXT | NOT NULL, min 3, max 80 chars | Nombre completo |
| email | TEXT | NOT NULL, UNIQUE, formato RFC 5322 | Correo electrónico |
| password | TEXT | NOT NULL, min 8 chars (hasheado SHA-256) | Contraseña hasheada |
| telefono | TEXT | NOT NULL, 10 dígitos, regex ^3[0-9]{9}$ | Teléfono colombiano |
| rol | TEXT | NOT NULL, enum: "comprador","vendedor","administrador" | Rol del usuario |
| activo | INTEGER | NOT NULL, default 1 (boolean) | Estado activo/inactivo |
| foto_url | TEXT | NULLABLE | Ruta local de foto de perfil |
| direccion | TEXT | NULLABLE, max 200 chars | Dirección de entrega |
| latitud | REAL | NULLABLE, -90 a 90 | Coordenada GPS |
| longitud | REAL | NULLABLE, -180 a 180 | Coordenada GPS |
| biometric_enabled | INTEGER | NOT NULL, default 0 (boolean) | Biometría activada |
| created_at | INTEGER | NOT NULL (epoch ms) | Fecha de creación |
| updated_at | INTEGER | NOT NULL (epoch ms) | Última modificación |

### Tabla: productos
| Campo | Tipo | Restricciones | Descripción |
|---|---|---|---|
| id | INTEGER | PK, AUTOINCREMENT | ID único |
| nombre | TEXT | NOT NULL, min 3, max 80 chars | Nombre del producto |
| descripcion | TEXT | NOT NULL, min 10, max 500 chars | Descripción |
| presentacion | TEXT | NOT NULL, enum: "300ml","500ml","1L","5L","20L" | Presentación |
| precio | REAL | NOT NULL, > 0, max 9999999.99 | Precio en COP |
| imagen_url | TEXT | NULLABLE | Ruta local de imagen |
| disponible | INTEGER | NOT NULL, default 1 (boolean) | Disponibilidad |
| stock | INTEGER | NOT NULL, >= 0 | Unidades disponibles |
| vendedor_id | INTEGER | NOT NULL, FK → usuarios.id | ID del vendedor |
| eliminado | INTEGER | NOT NULL, default 0 (boolean) | Soft delete |
| created_at | INTEGER | NOT NULL (epoch ms) | Fecha de creación |
| updated_at | INTEGER | NOT NULL (epoch ms) | Última modificación |

### Tabla: carrito
| Campo | Tipo | Restricciones | Descripción |
|---|---|---|---|
| id | INTEGER | PK, AUTOINCREMENT | ID único |
| usuario_id | INTEGER | NOT NULL, FK → usuarios.id | ID del comprador |
| producto_id | INTEGER | NOT NULL, FK → productos.id | ID del producto |
| cantidad | INTEGER | NOT NULL, min 1, max 99 | Cantidad seleccionada |
| precio_al_momento | REAL | NOT NULL | Precio capturado al agregar |
| created_at | INTEGER | NOT NULL (epoch ms) | Fecha de agregado |
| updated_at | INTEGER | NOT NULL (epoch ms) | Última modificación |

### Tabla: pedidos
| Campo | Tipo | Restricciones | Descripción |
|---|---|---|---|
| id | INTEGER | PK, AUTOINCREMENT | ID único |
| order_number | TEXT | NOT NULL, UNIQUE, formato "TA-YYYYMMDD-XXXX" | Número de pedido legible |
| usuario_id | INTEGER | NOT NULL, FK → usuarios.id | ID del comprador |
| total_productos | REAL | NOT NULL | Subtotal productos |
| costo_envio | REAL | NOT NULL, default 0 | Costo de envío |
| total_pedido | REAL | NOT NULL | Total final |
| direccion_entrega | TEXT | NOT NULL | Dirección de entrega |
| latitud | REAL | NULLABLE | Coordenada GPS |
| longitud | REAL | NULLABLE | Coordenada GPS |
| estado | TEXT | NOT NULL, enum: "pendiente","pagado","enviado","entregado","cancelado" | Estado actual |
| metodo_pago | TEXT | NOT NULL | Método de pago usado |
| transaction_id | TEXT | NULLABLE | Referencia de la pasarela |
| created_at | INTEGER | NOT NULL (epoch ms) | Fecha de creación |
| updated_at | INTEGER | NOT NULL (epoch ms) | Última modificación |

### Tabla: detalle_pedidos
| Campo | Tipo | Restricciones | Descripción |
|---|---|---|---|
| id | INTEGER | PK, AUTOINCREMENT | ID único |
| pedido_id | INTEGER | NOT NULL, FK → pedidos.id | ID del pedido |
| producto_id | INTEGER | NOT NULL, FK → productos.id | ID del producto |
| nombre_producto | TEXT | NOT NULL | Nombre (snapshot) |
| presentacion | TEXT | NOT NULL | Presentación (snapshot) |
| cantidad | INTEGER | NOT NULL, min 1 | Cantidad comprada |
| precio_unitario | REAL | NOT NULL | Precio al momento de compra |
| subtotal | REAL | NOT NULL | = cantidad × precio_unitario |
| vendedor_id | INTEGER | NOT NULL, FK → usuarios.id | ID del vendedor |

---

## Catálogo de Productos (Datos de Ejemplo)

| # | Producto | Contenido | Tipo | Precio Sugerido (COP) |
|---|---|---|---|---|
| 1 | Botella personal | 300 ml | Unidad | $1.500 |
| 2 | Botella mediana | 500 ml | Unidad | $2.500 |
| 3 | Botella familiar | 1 litro | Unidad | $4.000 |
| 4 | Botellón | 5 litros | Unidad | $12.000 |
| 5 | Pack personal | 24 × 300 ml | Paquete | $30.000 |
| 6 | Pack mediano | 12 × 500 ml | Paquete | $25.000 |
| 7 | Pack familiar | 6 × 1 litro | Paquete | $20.000 |
| 8 | Garrafón | 20 litros | Unidad | $18.000 |

---

## Navegación

### Flujo general:
```
SplashActivity → LoginActivity
                    ├── (Registro) → RegisterActivity → LoginActivity
                    ├── (Olvidé contraseña) → ForgotPasswordActivity → LoginActivity
                    └── (Login exitoso) → Según rol:
                        ├── "comprador"     → CompradorMainActivity (BottomNav)
                        ├── "vendedor"      → VendedorMainActivity (BottomNav)
                        └── "administrador" → AdminMainActivity (DrawerLayout)
```

### Comprador - BottomNavigationView:
```
BottomNav Items: Inicio | Catálogo | Carrito | Pedidos | Perfil
Cada tab tiene su propio NavGraph con fragmentos anidados.
```

### Vendedor - BottomNavigationView:
```
BottomNav Items: Productos | Pedidos | Perfil
```

### Administrador - NavigationDrawer:
```
Drawer Items: Dashboard | Usuarios | Productos | Reportes | Perfil | Cerrar Sesión
```

---

## Dependencias Requeridas (libs.versions.toml)

```toml
[versions]
agp = "8.2.0"
kotlin = "1.9.22"
coreKtx = "1.12.0"
appcompat = "1.6.1"
material = "1.11.0"
constraintlayout = "2.1.4"
recyclerview = "1.3.2"
cardview = "1.0.0"
navigationFragment = "2.7.7"
navigationUi = "2.7.7"
viewpager2 = "1.0.0"
room = "2.6.1"
lifecycle = "2.7.0"
biometric = "1.1.0"
glide = "4.16.0"
playServicesLocation = "21.1.0"
playServicesMaps = "18.2.0"
securityCrypto = "1.1.0-alpha06"

[libraries]
androidx-core-ktx = { group = "androidx.core", name = "core-ktx", version.ref = "coreKtx" }
androidx-appcompat = { group = "androidx.appcompat", name = "appcompat", version.ref = "appcompat" }
material = { group = "com.google.android.material", name = "material", version.ref = "material" }
androidx-constraintlayout = { group = "androidx.constraintlayout", name = "constraintlayout", version.ref = "constraintlayout" }
androidx-recyclerview = { group = "androidx.recyclerview", name = "recyclerview", version.ref = "recyclerview" }
androidx-cardview = { group = "androidx.cardview", name = "cardview", version.ref = "cardview" }
androidx-navigation-fragment = { group = "androidx.navigation", name = "navigation-fragment-ktx", version.ref = "navigationFragment" }
androidx-navigation-ui = { group = "androidx.navigation", name = "navigation-ui-ktx", version.ref = "navigationUi" }
androidx-viewpager2 = { group = "androidx.viewpager2", name = "viewpager2", version.ref = "viewpager2" }
room-runtime = { group = "androidx.room", name = "room-runtime", version.ref = "room" }
room-ktx = { group = "androidx.room", name = "room-ktx", version.ref = "room" }
room-compiler = { group = "androidx.room", name = "room-compiler", version.ref = "room" }
lifecycle-viewmodel = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-ktx", version.ref = "lifecycle" }
lifecycle-livedata = { group = "androidx.lifecycle", name = "lifecycle-livedata-ktx", version.ref = "lifecycle" }
biometric = { group = "androidx.biometric", name = "biometric", version.ref = "biometric" }
glide = { group = "com.github.bumptech.glide", name = "glide", version.ref = "glide" }
play-services-location = { group = "com.google.android.gms", name = "play-services-location", version.ref = "playServicesLocation" }
play-services-maps = { group = "com.google.android.gms", name = "play-services-maps", version.ref = "playServicesMaps" }
security-crypto = { group = "androidx.security", name = "security-crypto", version.ref = "securityCrypto" }

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
```

---

## Resources XML Requeridos

### res/values/strings.xml
Todos los textos de la app deben estar en strings.xml, nunca hardcodeados en layouts.

### res/values/dimens.xml
Dimensiones reutilizables: márgenes, paddings, tamaños de texto, radios de esquinas.

### res/values/themes.xml
Tema personalizado basado en Theme.Material3.DayNight con los colores del proyecto.

### res/drawable/
- `bg_rounded_card.xml` — Shape con esquinas redondeadas para CardViews
- `bg_button_primary.xml` — Selector de estados para botón principal
- `bg_input_field.xml` — Fondo para campos de texto
- `bg_splash.xml` — Fondo degradado para splash screen
- `ic_logo.xml` — Logo vectorial de TomenAguita
- `ic_water_bottle.xml` — Ícono de botella de agua
- Íconos de navegación: `ic_home`, `ic_cart`, `ic_orders`, `ic_profile`, `ic_products`, `ic_users`, `ic_dashboard`, `ic_reports`

### res/menu/
- `menu_bottom_nav_comprador.xml` — 5 ítems para BottomNav del comprador
- `menu_bottom_nav_vendedor.xml` — 3 ítems para BottomNav del vendedor
- `menu_drawer_admin.xml` — Ítems del NavigationDrawer del admin

### res/navigation/
- `nav_graph_comprador.xml` — NavGraph del comprador
- `nav_graph_vendedor.xml` — NavGraph del vendedor
- `nav_graph_admin.xml` — NavGraph del administrador

---

## Reglas de Negocio Clave

1. **Roles:** Al registrarse, el usuario es "comprador" por defecto. Solo un admin puede cambiar roles.
2. **Productos:** Solo vendedores y administradores pueden crear/editar productos. Un vendedor solo ve/edita sus propios productos.
3. **Carrito:** Solo el comprador tiene carrito. No puede agregar productos propios si también es vendedor.
4. **Pedidos:** El flujo de estados es estricto: pendiente → pagado → enviado → entregado. No se permite retroceso.
5. **Eliminación:** Siempre soft delete (campo `eliminado = true`). Nunca borrar registros físicamente.
6. **Biometría:** Opcional. Solo se habilita si el dispositivo tiene sensor y el usuario lo activa en configuración.
7. **Geolocalización:** Para dirección de entrega del comprador. Permiso en runtime.
8. **Cámara:** Para foto de producto (vendedor) y foto de perfil (todos). Usar cámara o galería.

---

## Datos de Ejemplo para Desarrollo

### Usuarios de prueba:
| Email | Password | Rol |
|---|---|---|
| admin@tomenaguita.com | Admin123! | administrador |
| vendedor@tomenaguita.com | Vendedor123! | vendedor |
| comprador@tomenaguita.com | Comprador123! | comprador |

### Nota sobre esta entrega:
Esta entrega (Actividad 3) se enfoca SOLO en el **frontend** (interfaces gráficas). Los datos deben estar hardcodeados o usar datos de ejemplo en los adaptadores. La lógica de negocio, base de datos funcional y conexión a APIs se implementarán en entregas posteriores. Sin embargo, la estructura de paquetes y clases debe estar preparada para esa integración.

---

## Requisitos de Estilo (de la actividad académica)

- Se deben utilizar **colores y propiedades diferentes a los que vienen por defecto** en Android Studio.
- Todo el diseño debe seguir los mockups creados en Figma en la Actividad 2.
- La interfaz debe seguir lineamientos de **Material Design 3**.
- Navegación máximo **3 toques** para llegar a cualquier funcionalidad desde el inicio.
- Mensajes de retroalimentación claros (Snackbar, Toast, TextInputLayout.error).
- Usar RecyclerView (nunca ListView) con CardView para listas.
- ConstraintLayout como layout principal en todas las pantallas.

---

## Archivos de Referencia

- Documento técnico completo: `../TomenAguita_DocumentoTecnico.md`
- Tabla de requerimientos: `../cuadros.xlsx` (hojas: "RF CORREGIDOS" y "RNF CORREGIDOS")
- Documento Word de la actividad 2: `../VERSION2 actividad 2 Documento técnico de planeación para tienda virtual Android.docx`
- Mockups: Proyecto de Figma de TomenAguita (acceder vía Figma MCP si disponible)
