# Guía y Libreto — Video Técnico: Desarrollo de Interfaces en Tomen Agüita

> **Tema:** Desarrollo de interfaces: XML, layouts, drawables, estilos y values
> **Duración máxima:** 5 minutos
> **Formato:** Grabación de pantalla de Android Studio + emulador con voz en off
> **Herramientas:** Android Studio, emulador API 34, OBS Studio o grabador nativo de Windows (Win+G)

---

## Estructura del video (5 minutos exactos)

| # | Segmento | Archivo(s) a mostrar | Tiempo |
|---|---|---|---|
| 1 | Introducción | Emulador — pantalla de login en vivo | 0:20 |
| 2 | Values: la base del sistema de diseño | `colors.xml` → `dimens.xml` → `strings.xml` | 1:00 |
| 3 | Themes y estilos personalizados | `themes.xml` | 1:00 |
| 4 | Drawables: fondos, formas y selectores | `bg_splash.xml` → `bg_button_primary.xml` → `bg_input_field.xml` → `bg_circle_avatar.xml` | 1:00 |
| 5 | Layouts: ConstraintLayout en práctica | `activity_login.xml` + vista Design | 1:20 |
| 6 | El resultado en el emulador | Emulador — login, splash, item_producto | 0:20 |

---

## Segmento 1 — Introducción (0:20)

### Pantalla a mostrar
Emulador con la app corriendo, mostrando la pantalla de login. Mientras hablas, no navegues — deja la imagen quieta.

### Libreto
> *"En este video explico cómo está construida la capa de interfaz de Tomen Agüita: los archivos XML de recursos, los layouts con ConstraintLayout, los drawables personalizados y el sistema de estilos basado en Material Design 3. Todo el código visual del proyecto sigue un sistema de diseño consistente que parte de los archivos de valores."*

---

## Segmento 2 — Values: la base del sistema de diseño (1:00)

### Pantalla a mostrar
Android Studio. Abre la carpeta `res/values/` en el panel Project y muestra los tres archivos en secuencia: `colors.xml`, `dimens.xml`, `strings.xml`. Haz zoom en el editor (Ctrl+Shift+`+`) para que el texto sea legible.

### Orden de navegación en pantalla
1. **`colors.xml`** — desplázate lentamente, señala con el cursor la sección `primary` / `on_primary` / `status_*`
2. **`dimens.xml`** — señala la escala tipográfica (`text_body` 14sp → `text_title` 22sp → `text_splash_title` 36sp) y los tokens de dimensiones (`button_height` 52dp, `corner_radius_button` 24dp)
3. **`strings.xml`** — muestra brevemente las secciones de mensajes y etiquetas

### Libreto
> *"Todo el sistema visual parte de tres archivos en la carpeta `values`.*
>
> *Primero, `colors.xml`. Acá están definidos todos los colores del proyecto — ningún valor hexadecimal aparece directo en un layout. Tenemos la paleta primaria en azul oscuro, `#1565C0`, los colores de superficie, los de error y éxito, y los cinco colores de estado de pedido: naranja para pendiente, azul para pagado, morado para enviado.*
>
> *Segundo, `dimens.xml`. Define la escala tipográfica completa — desde `text_body` en 14sp hasta `text_splash_title` en 36sp — y los tokens de espaciado: `margin_small` 8dp, `margin_medium` 16dp, y así. Esto garantiza que todos los márgenes y tamaños del proyecto sean consistentes entre pantallas.*
>
> *Tercero, `strings.xml`. Todos los textos de la app están centralizados aquí: títulos, mensajes de error, etiquetas de botones, mensajes del sistema. No hay ningún texto escrito directamente en el código Kotlin ni en los layouts."*

### Notas de producción
- Zoom al 125% o 150% en el editor de Android Studio antes de grabar
- Desplaza lento — 2–3 líneas por segundo máximo
- No es necesario leer cada línea, solo señalar las secciones mencionadas

---

## Segmento 3 — Themes y estilos personalizados (1:00)

### Pantalla a mostrar
Android Studio con `themes.xml` abierto. Muestra el archivo completo desplazándote de arriba hacia abajo una vez.

### Puntos clave a señalar con el cursor
- Línea 4: `parent="Theme.Material3.DayNight.NoActionBar"` — el tema base
- Líneas 5–16: los atributos del tema principal (`colorPrimary`, `android:statusBarColor`, etc.)
- Líneas 24–30: `Widget.TomenAguita.Button` — hereda de Material3 y sobrescribe `minHeight`, `cornerRadius`, `textAllCaps`
- Líneas 39–45: `Widget.TomenAguita.TextInputLayout` — los cuatro radios de esquina y el color del borde
- Líneas 47–57: `Widget.TomenAguita.EditText` — apunta al drawable `bg_input_field` como fondo
- Líneas 59–74: los tres `TextAppearance` personalizados (Title, Body, Price)

### Libreto
> *"El archivo `themes.xml` es donde se definen el tema global y los estilos reutilizables del proyecto.*
>
> *El tema principal, `Theme.TomenAguita`, hereda de `Theme.Material3.DayNight.NoActionBar`. Esto nos da Material Design 3 como base y nos permite sobreescribir solo lo que necesitamos: el color primario, el color de la barra de estado, y la familia tipográfica.*
>
> *Luego están los estilos personalizados. `Widget.TomenAguita.Button` extiende el botón de Material3 y le aplica una altura mínima de 52dp, esquinas redondeadas de 24dp — que es lo que le da esa forma de pastilla — y desactiva las mayúsculas automáticas.*
>
> *`Widget.TomenAguita.TextInputLayout` ajusta los cuatro radios de esquina a 8dp y fija el color del borde activo al color primario.*
>
> *Y los tres `TextAppearance` — Title, Body y Price — definen los estilos de texto que se reutilizan en toda la app. Price, por ejemplo, siempre es negrita, color primario y tamaño `text_large`.*
>
> *Al centralizar estos estilos acá, si necesito cambiar el radio de los botones en toda la app, solo edito una línea."*

---

## Segmento 4 — Drawables: fondos, formas y selectores (1:00)

### Pantalla a mostrar
Android Studio. Abre la carpeta `res/drawable/` y muestra estos cuatro archivos en orden. Ábrelos uno por uno en el editor.

### Archivos y qué señalar

**`bg_splash.xml`** — gradiente diagonal
```xml
<gradient
    android:startColor="@color/splash_start"
    android:endColor="@color/splash_end"
    android:angle="135" />
```
Señala: el `angle="135"` (diagonal), los dos colores que vienen de `colors.xml`.

**`bg_button_primary.xml`** — selector con estados
```xml
<selector>
    <item android:state_pressed="true"> ... color primary_dark ... </item>
    <item android:state_enabled="false"> ... color divider ... </item>
    <item> ... color primary ... </item>  ← estado normal
</selector>
```
Señala: los tres estados — pressed, disabled, normal.

**`bg_input_field.xml`** — shape con borde
```xml
<shape android:shape="rectangle">
    <solid android:color="@color/input_background" />
    <corners android:radius="@dimen/corner_radius_small" />
    <stroke android:width="1dp" android:color="@color/divider" />
</shape>
```
Señala: `solid`, `corners`, `stroke` — las tres propiedades de un campo de texto.

**`bg_circle_avatar.xml`** — oval simple
```xml
<shape android:shape="oval">
    <solid android:color="@color/primary_light" />
</shape>
```
Señala: `shape="oval"` como forma base del avatar.

### Libreto
> *"Los drawables son los elementos gráficos definidos en XML — fondos, formas, íconos vectoriales y selectores de estado.*
>
> *El primero es `bg_splash.xml`, el fondo del splash screen. Es un `shape` con un gradiente diagonal a 135 grados, que va del azul oscuro `splash_start` al celeste `splash_end`. Los colores no están escritos acá — vienen de `colors.xml`.*
>
> *El segundo es `bg_button_primary.xml`. Este es un `selector`, que es un drawable que cambia según el estado del botón. Tiene tres casos: cuando el botón está presionado usa `primary_dark`, cuando está deshabilitado usa el color `divider` que es gris, y en estado normal usa `primary`. Esto le da feedback visual al usuario sin escribir una sola línea de Kotlin.*
>
> *El tercero es `bg_input_field.xml`. Define el aspecto de los campos de texto simples: fondo claro con `solid`, esquinas redondeadas con `corners`, y un borde delgado con `stroke`. Todo apuntando a los tokens de `colors.xml` y `dimens.xml`.*
>
> *Y el cuarto, `bg_circle_avatar.xml`, es simplemente un `oval` con relleno azul claro — es el fondo circular del ícono de usuario en las tarjetas."*

---

## Segmento 5 — Layouts: ConstraintLayout en práctica (1:20)

### Pantalla a mostrar
Android Studio con `activity_login.xml` abierto. Muestra primero la pestaña **Code** (XML), luego cambia a **Design** para que se vea la pantalla renderizada. Termina volviendo al código para señalar partes específicas.

### Puntos clave a señalar en el XML

**Línea 10 — ConstraintLayout dentro de NestedScrollView:**
> El `NestedScrollView` exterior permite que la pantalla sea scrolleable en dispositivos pequeños. El `ConstraintLayout` interior organiza los elementos con restricciones.

**Línea 15–24 — ImageView del logo:**
```xml
android:layout_width="@dimen/icon_size_logo"   ← 80dp, token de dimens
android:src="@drawable/ic_logo"                 ← drawable vectorial
android:contentDescription="@string/content_desc_logo"  ← accesibilidad
app:layout_constraintTop_toTopOf="parent"
app:layout_constraintStart_toStartOf="parent"
app:layout_constraintEnd_toEndOf="parent"       ← centrado horizontal
```

**Líneas 51–69 — TextInputLayout:**
```xml
style="@style/Widget.TomenAguita.TextInputLayout"  ← estilo personalizado
android:hint="@string/hint_email"                  ← texto de `strings.xml`
app:startIconDrawable="@android:drawable/ic_dialog_email"
```

**Líneas 103–112 — MaterialButton principal:**
```xml
style="@style/Widget.TomenAguita.Button"        ← estilo del themes.xml
android:layout_height="@dimen/button_height"    ← token de dimens
android:text="@string/btn_login"                ← texto de strings.xml
```

**Líneas 114–125 — Botón outlined (biométrico):**
```xml
style="@style/Widget.TomenAguita.Button.Outlined"  ← variante outlined
app:icon="@drawable/ic_profile"
app:iconGravity="start"
```

### Libreto
> *"El layout de login es el ejemplo más completo para ver cómo se integran todos los componentes.*
>
> *La estructura raíz es un `NestedScrollView` que envuelve al `ConstraintLayout`. Esto es importante: garantiza que en pantallas pequeñas el usuario pueda hacer scroll para ver todos los campos, sin que el teclado tape el botón de login.*
>
> *Dentro del `ConstraintLayout`, cada vista se posiciona mediante restricciones. El logo, por ejemplo, está centrado horizontalmente con `constraintStart_toStartOf="parent"` y `constraintEnd_toEndOf="parent"`. Su tamaño viene del token `@dimen/icon_size_logo`, que vale 80dp.*
>
> *Los campos de texto usan `TextInputLayout` de Material3 con el estilo `Widget.TomenAguita.TextInputLayout`. El `hint` viene de `strings.xml`, y el ícono decorativo es uno del sistema de Android. Al campo de contraseña se le agrega `app:endIconMode="password_toggle"` — eso genera el ojo de mostrar/ocultar sin código adicional.*
>
> *Los dos botones muestran los dos estilos definidos en `themes.xml`: el de login usa `Widget.TomenAguita.Button` con fondo sólido, y el de huella usa `Widget.TomenAguita.Button.Outlined` con borde. Ambos tienen la misma altura — `@dimen/button_height`, 52dp — para que la experiencia táctil sea consistente.*
>
> *[Cambiar a pestaña Design] En la vista de diseño podemos ver el resultado visual directamente en Android Studio, sin necesidad de compilar."*

### Notas de producción
- Al cambiar entre Code y Design, hazlo lentamente para que se note la transición
- En la vista Design, señala con el cursor el logo, los campos, y los botones mientras los mencionas
- Si el emulador está corriendo, puedes alternar rápido para mostrar el resultado real

---

## Segmento 6 — El resultado en el emulador (0:20)

### Pantalla a mostrar
Emulador en vivo. Muestra en este orden, sin navegar demasiado:
1. Splash screen (el gradiente diagonal)
2. Pantalla de login (logo, campos, botones)
3. Navega a la lista de productos y muestra una tarjeta `item_producto` (imagen, nombre, precio en azul, botón "+")

### Libreto
> *"El resultado de todo este sistema es lo que vemos en pantalla. El gradiente del splash viene del drawable. Los campos y botones del login respetan los estilos de `themes.xml`. Los precios en azul negrita son el `TextAppearance.TomenAguita.Price`. Y las tarjetas de productos usan el `corner_radius_medium` de 12dp definido en `dimens.xml`.*
>
> *Todo consistente, todo centralizado, todo modificable desde un solo lugar."*

---

## Tips de grabación

### Configuración de Android Studio antes de grabar
- Cierra paneles innecesarios (Terminal, Logcat) — solo el editor y el panel Project
- Usa el tema **Darcula** (oscuro) — se ve mejor en video y contrasta más con el código
- Zoom del editor al **125%**: `File → Settings → Editor → Font → Size: 16`
- Aumenta el tamaño del cursor: `Settings → Editor → General → Show cursor animation`

### Orden recomendado de archivos abiertos
Ábrelos todos antes de grabar y navega entre ellos con Ctrl+Tab:
1. `colors.xml`
2. `dimens.xml`
3. `strings.xml`
4. `themes.xml`
5. `bg_splash.xml`
6. `bg_button_primary.xml`
7. `bg_input_field.xml`
8. `bg_circle_avatar.xml`
9. `activity_login.xml`

### Tiempo de habla vs. silencio
- Al abrir un archivo nuevo, dale 1–2 segundos de silencio antes de hablar para que el espectador se ubique
- Cuando señales código con el cursor, hazlo despacio — el ojo humano necesita ~1 segundo para seguir el movimiento en pantalla

### Edición
- No necesitas cortar o editar mucho si grabas en orden — los saltos entre archivos son naturales
- Agrega el nombre del archivo actual como **subtítulo fijo** en pantalla durante cada segmento (ej. `themes.xml`) para que el espectador siempre sepa qué está viendo

---

*Guía elaborada para el proyecto Tomen Agüita — Fundación Universitaria Compensar · 2025*
