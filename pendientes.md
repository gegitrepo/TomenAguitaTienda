# PENDIENTES — TomenAgüita

Instrucciones para Claude Code. Lee este archivo al inicio de cada sesión de desarrollo.
Ejecuta las tareas en el orden numerado. Marca cada una como ✅ cuando quede completamente implementada.

---

## CONTEXTO DEL PROYECTO

App Android (Kotlin + MVVM) para tienda de agua purificada. Tres roles:
- **Comprador**: navega productos, compra, gestiona pedidos
- **Vendedor**: gestiona sus productos y atiende pedidos
- **Administrador**: gestiona usuarios, productos y reportes

Stack: Room (local) + Firestore (nube) con escritura dual. ViewModels + LiveData. Navigation Component + SafeArgs. Mercado Pago Checkout Pro via WebView. Google Maps SDK. Firebase Auth + Storage.

Directorio raíz: `C:\Users\ASUS\Desktop\appMovil\Tomen aguita`

---

## ESTADO ACTUAL — Lo que ya funciona ✅

### Comprador
- Registro y login con Firebase Auth
- Home con 4 productos aleatorios destacados + búsqueda funcional
- Catálogo completo con búsqueda y filtro
- Detalle de producto con imagen (Glide)
- Carrito: agregar, incrementar cantidad, eliminar ítem, vaciar, badge contador
- Resumen de pedido: nombre/teléfono pre-llenados, dirección auto-detectada con GPS + campo manual, mapa Google Maps con marcador, total
- Pasarela de pago Mercado Pago via WebView (flujo implementado, ver problemas conocidos)
- Historial de pedidos con filtros por estado (pendiente/pagado/enviado/entregado/todos); los cancelados se ocultan del filtro "Todos"
- Detalle de pedido: productos comprados, dirección, fecha/hora, total, chip de estado
- Cancelar pedido pendiente con confirmación (AlertDialog) → desaparece del historial
- "Completar este pedido": recarga los productos del pedido pendiente al carrito y navega al tab de carrito
- Editar perfil con foto (cámara o galería, sube a Firebase Storage)

### Vendedor
- Login
- Mis Productos: lista de productos propios, crear nuevo, editar, eliminar con confirmación
- Crear/Editar producto: nombre, presentación, precio, stock, imagen (cámara/galería → Firebase Storage)
- Pedidos recibidos: lista de todos los pedidos del sistema (ver pendiente #3)
- Detalle de pedido: número, fecha, estado, info del comprador (nombre, teléfono, dirección), lista de productos comprados, total
- Avanzar estado: botón "Marcar enviado" (solo si pagado) y "Marcar entregado" (solo si enviado), con Snackbar de confirmación. Actualiza Room + Firestore.

### Administrador
- Login
- Gestión de usuarios: lista con búsqueda, crear usuario, editar, desactivar
- Gestión de productos: lista con búsqueda, editar, eliminar con confirmación
- Reporte de ventas: total del día, total del mes, lista de últimos 20 pedidos

### Capa de datos
- Room con entidades: Usuario, Producto, Pedido, DetallePedido, CarritoItem
- Firestore: colección `pedidos/{orderNumber}` con subcolección `detalles/`, colección `usuarios/`, colección `productos/`
- Sincronización dual Room ↔ Firestore en todas las operaciones de escritura
- Carrito aislado por userId; se limpia al cerrar sesión y al iniciar sesión nueva

---

## TAREAS PENDIENTES — Ejecutar en este orden

### TAREA 1 — Resumen de pedido: mostrar lista de productos 🔴 BLOCKER
**Problema:** `fragment_resumen_pedido.xml` tiene `@+id/rvResumen` (RecyclerView) pero `ResumenPedidoFragment.kt` nunca lo enlaza ni le asigna adaptador. El comprador no ve qué está comprando antes de pagar.

**Qué hacer:**
1. En `ResumenPedidoFragment.kt`: crear instancia de `DetallePedidoAdapter` (ya existe en `ui/adapter/DetallePedidoAdapter.kt`).
2. Enlazar `binding.rvResumen` con `LinearLayoutManager` y el adaptador.
3. Dentro del observer de `carritoViewModel.getCarrito(userId)`, una vez que `carritoItems` se actualiza, construir la lista de `DetallePedido` usando `productoViewModel.productosDisponibles.value` como mapa de lookup (mismo patrón que ya hace `confirmarPedido()`), y llamar `adapter.submitList(detalles)`.
4. El `rvResumen` ya tiene `nestedScrollingEnabled="false"` en el XML — no tocar eso.

**Archivos a modificar:**
- `app/src/main/java/com/example/tomenaguita/ui/comprador/pago/ResumenPedidoFragment.kt`

---

### TAREA 2 — Perfil del vendedor: formulario de edición funcional 🔴 BLOCKER
**Problema:** `PerfilVendedorFragment.kt` línea 32: el botón "Editar perfil" solo muestra un Snackbar — no navega a ningún formulario.

**Qué hacer:**
1. Revisar si `nav_graph_vendedor.xml` ya tiene destino para edición de perfil. Si no, agregarlo.
2. La clase `EditarPerfilFragment` del comprador (`ui/comprador/perfil/EditarPerfilFragment.kt`) ya hace exactamente lo que necesita el vendedor (edita nombre, teléfono, foto en Firestore + Storage). Reutilizarla: agregar ese fragment como destino en el nav graph del vendedor.
3. En `PerfilVendedorFragment.kt`: reemplazar el `showSnackbar` por `findNavController().navigate(R.id.action_perfilVendedor_to_editarPerfil)`.
4. Agregar la importación de `findNavController` si falta.

**Archivos a modificar:**
- `app/src/main/res/navigation/nav_graph_vendedor.xml`
- `app/src/main/java/com/example/tomenaguita/ui/vendedor/perfil/PerfilVendedorFragment.kt`

---

### TAREA 3 — Pedidos del vendedor: filtrar solo los propios 🔴 BLOCKER
**Problema:** `PedidosRecibidosFragment.kt` llama `viewModel.getAllPedidos()` y muestra todos los pedidos del sistema. En un escenario multi-vendedor cada vendedor ve los pedidos de los demás.

**Contexto importante:** Los pedidos se crean en Room con `usuarioId` (Long del comprador) y en Firestore con `usuarioUid`. El vendedor no tiene un campo directo en el pedido, pero cada `DetallePedido` tiene `vendedorId` (Long). El filtro más correcto es: mostrar los pedidos donde al menos un `DetallePedido.vendedorId` coincida con el `vendedorId` del vendedor activo.

**Qué hacer:**
1. En `PedidoDao.kt`: agregar query que filtre pedidos que contengan detalles del vendedor:
   ```sql
   SELECT DISTINCT p.* FROM pedidos p
   INNER JOIN detalle_pedidos d ON d.pedidoId = p.id
   WHERE d.vendedorId = :vendedorId
   ORDER BY p.createdAt DESC
   ```
2. En `PedidoRepository.kt`: exponer ese query como `Flow<List<Pedido>>`.
3. En `PedidoViewModel.kt`: agregar `fun getPedidosByVendedor(vendedorId: Long)`.
4. En `PedidosRecibidosFragment.kt`: obtener `vendedorId` de `SessionManager(requireContext()).getUserId()` y llamar `viewModel.getPedidosByVendedor(vendedorId)` en lugar de `getAllPedidos()`.

**Archivos a modificar:**
- `app/src/main/java/com/example/tomenaguita/data/database/dao/PedidoDao.kt`
- `app/src/main/java/com/example/tomenaguita/data/repository/PedidoRepository.kt`
- `app/src/main/java/com/example/tomenaguita/viewmodel/PedidoViewModel.kt`
- `app/src/main/java/com/example/tomenaguita/ui/vendedor/pedidos/PedidosRecibidosFragment.kt`

---

### TAREA 4 — Perfil del comprador: cargar teléfono y dirección reales 🟡 DETALLE
**Problema:** `PerfilFragment.kt` líneas 36-37 muestran siempre `demo_phone` y `demo_address`. `EditarPerfilFragment` sí guarda estos datos en Firestore, pero al volver al perfil no se ven.

**Qué hacer:**
1. En `PerfilFragment.onViewCreated()`: después de cargar nombre y email desde `SessionManager`, hacer una llamada a Firestore para obtener `telefono` y `direccion` del documento `usuarios/{uid}`.
2. Usar `lifecycleScope.launch` + `FirebaseFirestore.getInstance().collection("usuarios").document(uid).get().await()` (mismo patrón que ya usa `ResumenPedidoFragment.cargarTelefonoFirestore()`).
3. Si el campo está vacío o la llamada falla, dejar los strings demo como fallback.

**Archivos a modificar:**
- `app/src/main/java/com/example/tomenaguita/ui/comprador/perfil/PerfilFragment.kt`

---

### TAREA 5 — Estados vacíos en listas principales 🟡 DETALLE
**Problema:** Varias pantallas muestran un RecyclerView en blanco cuando no hay datos, sin ningún mensaje al usuario.

**Pantallas afectadas y qué mostrar:**

| Fragment | Mensaje sugerido |
|---|---|
| `HomeFragment` | "No hay productos disponibles" (solo si lista vacía tras carga) |
| `CatalogoFragment` | "No se encontraron productos" (también al filtrar sin resultados) |
| `MisProductosFragment` (vendedor) | "Aún no tienes productos. Toca + para crear uno" |
| `PedidosRecibidosFragment` (vendedor) | "No tienes pedidos recibidos aún" |
| `HistorialPedidosFragment` (comprador) | "No tienes pedidos en este estado" |
| `GestionProductosFragment` (admin) | "No hay productos registrados" |
| `ListaUsuariosFragment` (admin) | "No se encontraron usuarios" |

**Cómo hacerlo (patrón consistente):**
1. En cada XML: agregar un `TextView` con id `tvEmpty`, centrado, con `visibility="gone"`.
2. En cada Fragment: cuando `adapter.submitList(lista)` recibe lista vacía → `tvEmpty.visible()` + `rvXxx.gone()`; cuando tiene datos → `tvEmpty.gone()` + `rvXxx.visible()`.
3. Usar las extensiones `visible()` y `gone()` que ya existen en `utils/Extensions.kt`.

---

### TAREA 6 — Mercado Pago: resolver fallo en sandbox ⚠️ CONOCIDO
**Estado actual:** El WebView carga el checkout pero el pago falla con "No pudimos procesar tu pago".

**Contexto acumulado de intentos anteriores:**
- El `payer.email` fue eliminado del body de la preferencia para forzar login del comprador en el checkout
- Las credenciales actuales: token de vendedor test `APP_USR-7127794535848181-...` en `Constants.kt`
- Tarjeta de prueba configurada: Mastercard `5254 1336 7440 3564`, CVV `123`, venc `11/25`
- Cuentas test: Vendedor `TESTUSER8879678697068368906` / Comprador `TESTUSER8879678697068368906`
- El error más probable es que el WebView no permite login de la cuenta comprador de prueba, o que las URLs de retorno no coinciden exactamente

**Qué investigar/intentar:**
1. Verificar en `MercadoPagoHelper.kt` que `back_urls` usen exactamente `tomenaguita.app/pago/success`, `tomenaguita.app/pago/failure`, `tomenaguita.app/pago/pending`
2. Verificar que `WebViewClient.shouldOverrideUrlLoading` intercepta esas URLs correctamente en `PasarelaPagoFragment.kt`
3. Considerar agregar `auto_return = "approved"` al body de la preferencia para forzar redirección automática al aprobar
4. Si el problema persiste, implementar flujo alternativo: después de crear el pedido, cambiar estado directamente a "pagado" con un botón de simulación visible solo en modo debug, para poder probar el resto del flujo

---

### TAREA 7 — Google Maps: mapa muestra tiles beige ⚠️ CONOCIDO
**Estado:** El mapa carga y el marcador se coloca correctamente, pero los tiles del mapa son beige (sin imagen satelital/calles).

**Causa confirmada:** La API key `AIzaSyDjSmwJeFf-ajcMsbU1RBcZFSmcuz4QTSg` está activa y sin restricciones, pero Maps SDK for Android requiere que la cuenta de Google Cloud tenga facturación habilitada.

**Qué hacer:**
- Esta es una configuración externa a la app. El usuario debe habilitar facturación en Google Cloud Console para el proyecto que contiene la API key.
- No hay cambios de código necesarios a menos que se quiera mostrar un mensaje al usuario cuando el mapa no carga tiles (cosmético).

---

### TAREA 8 — Comprobación final y limpieza 🔵 AL FINAL
**Hacer solo después de completar tareas 1-5:**

1. Verificar que todas las pantallas compilan sin warnings de binding null
2. Confirmar que el flujo completo de compra funciona end-to-end (excepto MP payment si sigue sin resolverse)
3. Confirmar que el flujo del vendedor funciona: ver solo sus pedidos → avanzar estados
4. Confirmar que admin puede gestionar usuarios y productos sin afectar vendedorId
5. Revisar strings hardcodeados restantes y moverlos a `strings.xml`
6. Inicializar repositorio git y hacer commit inicial (el usuario debe confirmar antes de ejecutar)

---

## PROBLEMAS CONOCIDOS QUE NO REQUIEREN CAMBIO DE CÓDIGO

| Problema | Causa | Solución |
|---|---|---|
| Nombre/teléfono del comprador vacíos en vista del vendedor | Room es local; el `usuarioId` del comprador en el dispositivo del vendedor no coincide | Requiere sincronizar usuarios desde Firestore a Room en el lado del vendedor. Alternativa: guardar nombre/teléfono directamente en el documento del pedido en Firestore al crearlo |
| Google Maps beige | Facturación no habilitada en Google Cloud | Acción del usuario en Google Cloud Console |
| Mercado Pago sandbox fallando | Ver Tarea 6 | Ver Tarea 6 |

---

## NOTAS DE ARQUITECTURA — Patrones a seguir

- **LiveData sticky**: usar `MutableLiveData<T?>`, en el observer hacer `val x = it ?: return@observe`, y llamar `viewModel.clearX()` al consumir el evento
- **Carrito vacío bug**: nunca usar `getCarrito(userId).value` directamente — siempre almacenar en variable local via observer
- **Escritura dual**: toda operación Room debe intentar replicarse en Firestore con `try { } catch (_: Exception) { }` para no bloquear si no hay red
- **Imágenes**: Glide con `placeholder` y `error` drawable. URLs de Firebase Storage
- **Navegación**: no usar `saveState/restoreState` en el bottom nav para evitar que sub-destinos queden guardados al cambiar de tab
- **VendedorId en productos**: al editar, siempre preservar `productoActual?.vendedorId ?: session.getUserId()` — nunca sobreescribir con el userId del admin
