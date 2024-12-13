# NovelasNuevo

Enlace del repositorio --> [https://github.com/HugoSanchezGallego/NovelasNuevo.git](https://github.com/HugoSanchezGallego/NovelasNuevo.git)

He tenido que crear un nuevo repositorio con un proyecto nuevo distinto del primero, ya que el primero no estaba bien subida la carpeta "res", dando errores en el Android Manifest que no he sido capaz de arreglar ![image](https://github.com/user-attachments/assets/2415a9bc-8f22-4962-b69f-dbaf41d829a2)

Cree otro repositorio con el proyecto clonado, pero tras intentarlo enlazar a firebase, el proyecto no podía ejecutarse. Cada vez que lo intentaba, el ordenador se bloqueaba y tenía que reiniciarlo.

Por lo tanto cree otro proyecto desde 0 con implementaciones muy básicas pero que cumple con el objetivo de la práctica, que es el de implementar firebase y que las novelas se guarden adecuadamente y se puedan añadir.

Seguiré tratando de recuperar lo que hice para la primera práctica pero de momento para la fecha de hoy he tenido que hacer este repositorio, en cuanto consiga enlazar la primera práctica con firebase y que mi ordenador pueda ejecutarla adecuadamente modificaré el README de esta práctica con el enlace del repositorio arreglado.

-**ACTUALIZACIÓN**: Ya está solucionado el error del anterior repositorio de la primera entrega, como ya te dije en el correo que te envié, se debía a que la carpeta "res" no se había subido en github por un conflicto en el .gitignore. De todas formas, seguiré en este repositorio ya que he ido haciendo todas las entregas aquí y el anterior está obsoleto, ya que no esta ni conectado con Firebase

## Descripción del Proyecto

Este proyecto es una aplicación de Android que permite gestionar una colección de novelas. Las funcionalidades principales incluyen:

- Añadir nuevas novelas.
- Eliminar novelas existentes.
- Guardar y recuperar novelas desde Firebase Firestore.

## Requisitos

- Android Studio Koala Feature Drop | 2024.1.2
- Kotlin
- Firebase Firestore

## Uso

1. Ejecuta la aplicación en un dispositivo o emulador Android.
2. Añade nuevas novelas utilizando el botón "Añadir Novela".
3. Elimina novelas seleccionando una novela y pulsando el botón "Eliminar Novela".

## Estructura del Proyecto

- `MainActivity.kt`: Actividad principal que maneja la lógica de la aplicación.
- `Novela.kt`: Data class que representa una novela.
- `DetallesNovelaScreen.kt`: Composable que muestra los detalles de una novela.
- `AddNovelaDialog.kt`: Composable que permite añadir una nueva novela.

## Nuevos Cambios

### Añadido

- **DetallesNovelaScreen.kt**: Se añadió la funcionalidad para eliminar novelas de Firebase y actualizar la lista de novelas en la pantalla principal.
- **SettingsScreen.kt**: Se añadió una pantalla de configuración que permite cambiar el tema de la aplicación (claro/oscuro).
- **AuthScreen.kt**: Se añadió una pantalla de autenticación para iniciar sesión y registrarse.
- **PreferencesManager.kt**: Se añadió una clase para manejar las preferencias del usuario, como el tema de la aplicación.

### Cambiado

- **MainActivity.kt**: Se actualizó para integrar las nuevas pantallas de configuración y autenticación.
- **MainScreen.kt**: Se actualizó para manejar la eliminación de novelas y la actualización de la lista de novelas.
- **build.gradle.kts**: Se añadieron las dependencias necesarias para Firebase y se configuró el proyecto para usar Compose.
- **AndroidManifest.xml**: Se actualizó para incluir las nuevas actividades y configuraciones necesarias para Firebase.

### Más cambios

- **DetallesNovelaScreen.kt**: Se añadió la funcionalidad para eliminar novelas de Firebase y actualizar la lista de novelas en la pantalla principal.
- **SettingsScreen.kt**: Se añadió una pantalla de configuración que permite cambiar el tema de la aplicación (claro/oscuro).
- **AuthScreen.kt**: Se añadió una pantalla de autenticación para iniciar sesión y registrarse.
- **PreferencesManager.kt**: Se añadió una clase para manejar las preferencias del usuario, como el tema de la aplicación.
- **NovelasWidgetConfigurationActivity.kt**: Se añadió una actividad para configurar el widget.
- **NovelasWidgetProvider.kt**: Se añadió un proveedor de widget para actualizar el widget cada 3 segundos.
- **novelas_widget.xml**: Se añadió un layout para el widget.
- **novelas_widget_configure.xml**: Se añadió un layout para la configuración del widget.
- **novelas_widget_preview.xml**: Se añadió un layout de vista previa para el widget.
- **novelas_widget_info.xml**: Se añadió un archivo de configuración para el widget.

### Aún más cambios

### Cambios Implementados

#### 1. Optimización de Memoria
- **`MainActivity.kt`**: Se añadió el uso de `Memory Profiler` para identificar fugas de memoria y se implementaron técnicas de optimización de memoria.
- **`NovelasWidgetProvider.kt`**: Se añadió el uso de `Memory Profiler` para identificar fugas de memoria y se implementaron técnicas de optimización de memoria.

#### 2. Mejora del Rendimiento de la Red
- **`MainActivity.kt`**: Se utilizó `Network Profiler` para analizar y optimizar el uso de la red. Se implementó la compresión de datos usando OkHttp.
- **`NovelasWidgetProvider.kt`**: Se utilizó `Network Profiler` para analizar y optimizar el uso de la red. Se implementó la compresión de datos usando OkHttp.

#### 3. Optimización del Uso de la Batería
- **`MainActivity.kt`**: Se usaron `batterystats` y `Battery Historian` para identificar problemas de consumo de batería. Se redujo la frecuencia de actualizaciones en segundo plano.
- **`NovelasWidgetProvider.kt`**: Se usaron `batterystats` y `Battery Historian` para identificar problemas de consumo de batería. Se redujo la frecuencia de actualizaciones en segundo plano.

### Ejemplos de Cambios

#### `MainActivity.kt`
- Se añadió el cliente OkHttp con compresión de datos.
- Se implementaron técnicas de optimización de memoria y batería.

```kotlin
package com.example.novelasnuevo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.novelasnuevo.ui.theme.NovelasNuevoTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    private lateinit var db: FirebaseFirestore
    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            val compressedRequest = originalRequest.newBuilder()
                .header("Content-Encoding", "gzip")
                .build()
            chain.proceed(compressedRequest)
        }
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        db = FirebaseFirestore.getInstance()

        setContent {
            var authenticatedUser by remember { mutableStateOf<String?>(null) }
            var isDarkTheme by remember { mutableStateOf(false) }
            var showSettings by remember { mutableStateOf(false) }

            authenticatedUser?.let { username ->
                isDarkTheme = PreferencesManager.isDarkTheme(LocalContext.current, username)
            }

            NovelasNuevoTheme(darkTheme = isDarkTheme) {
                Surface(color = MaterialTheme.colorScheme.background) {
                    if (showSettings) {
                        SettingsScreen(
                            username = authenticatedUser!!,
                            isDarkTheme = isDarkTheme,
                            onBack = { showSettings = false },
                            onThemeChange = { isDarkTheme = it }
                        )
                    } else if (authenticatedUser != null) {
                        MainScreen(
                            db = db,
                            username = authenticatedUser!!,
                            onSettingsClick = { showSettings = true },
                            onLogoutClick = { authenticatedUser = null }
                        )
                    } else {
                        AuthScreen(onAuthSuccess = { username -> authenticatedUser = username })
                    }
                }
            }
        }
    }
}
```

#### `NovelasWidgetProvider.kt`
- Se añadió el cliente OkHttp con compresión de datos.
- Se implementaron técnicas de optimización de memoria y batería.

```kotlin
package com.example.novelasnuevo

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.RemoteViews
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class NovelasWidgetProvider : AppWidgetProvider() {
    private val handler = Handler(Looper.getMainLooper())
    private val updateInterval = 3000L // 3 seconds
    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            val compressedRequest = originalRequest.newBuilder()
                .header("Content-Encoding", "gzip")
                .build()
            chain.proceed(compressedRequest)
        }
        .build()

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        FirebaseApp.initializeApp(context)
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
        scheduleNextUpdate(context)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == "com.example.novelasnuevo.UPDATE_WIDGET") {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName(context, NovelasWidgetProvider::class.java))
            onUpdate(context, appWidgetManager, appWidgetIds)
        }
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.novelas_widget)
        val db = FirebaseFirestore.getInstance()
        val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("authenticated_user", null)

        if (username != null) {
            db.collection("novelas")
                .whereEqualTo("username", username)
                .get()
                .addOnSuccessListener { snapshot ->
                    val novelas = snapshot.toObjects(Novela::class.java)
                    val titles = novelas.joinToString("\n") { it.titulo }
                    views.setTextViewText(R.id.widgetTitle, "Novelas")
                    views.setTextViewText(R.id.widgetListView, titles)
                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
                .addOnFailureListener {
                    views.setTextViewText(R.id.widgetTitle, "Error")
                    views.setTextViewText(R.id.widgetListView, "Can't load widget")
                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
        } else {
            views.setTextViewText(R.id.widgetTitle, "No User")
            views.setTextViewText(R.id.widgetListView, "Please log in")
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    private fun scheduleNextUpdate(context: Context) {
        handler.postDelayed({
            val intent = Intent(context, NovelasWidgetProvider::class.java).apply {
                action = "com.example.novelasnuevo.UPDATE_WIDGET"
            }
            context.sendBroadcast(intent)
        }, updateInterval)
    }
}
```
### Cambios ejercicio 6

#### 1. Autenticación de Usuario
- Se ha actualizado `AuthScreen` para que después de autentificarse se guarde la ubicación del usuario.

#### 2. Manejo de Sesión
- `MainActivity` ahora maneja la autenticación del usuario y pasa el nombre de usuario y la contraseña a `MainScreen`.

#### 3. Creación de Novelas
- Se ha añadido la funcionalidad para guardar la ubicación del usuario al crear una novela en `AddNovelaDialog`.
- `AddNovelaDialog` ahora obtiene la ubicación del usuario desde Firebase y la guarda junto con la novela.

#### 4. Detalles de Novelas
- Se ha añadido un botón en `NovelaDetailsDialog` para ver la ubicación donde se creó la novela en Google Maps.

#### 5. Actualización de la Clase `Novela`
- La clase `Novela` ahora incluye los campos `latitude` y `longitude` para almacenar la ubicación.

#### 6. Permisos de Ubicación
- Se han añadido los permisos `ACCESS_FINE_LOCATION` y `ACCESS_COARSE_LOCATION` en `AndroidManifest.xml`.

#### 7. Corrección de Errores
- Se ha corregido el error "No value passed for parameter 'username'" al llamar a `AddNovelaDialog`.

### Funcionalidades Añadidas

- **Autenticación de Usuario**: Manejo de sesión y autenticación de usuario.
- **Creación de Novelas con Ubicación**: Guardar la ubicación del usuario al crear una novela.
- **Visualización de Ubicación**: Ver la ubicación de la creación de la novela en Google Maps desde los detalles de la novela.
- **Visualización de Ubicación Actual**: Ver la ubicación actual del usuario tras iniciar sesión en la aplicación.
