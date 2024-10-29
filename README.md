# NovelasNuevo

Enlace del repositorio --> [https://github.com/HugoSanchezGallego/NovelasNuevo.git](https://github.com/HugoSanchezGallego/NovelasNuevo.git)

He tenido que crear un nuevo repositorio con un proyecto nuevo distinto del primero, ya que el primero no estaba bien subida la carpeta "res", dando errores en el Android Manifest que no he sido capaz de arreglar ![image](https://github.com/user-attachments/assets/2415a9bc-8f22-4962-b69f-dbaf41d829a2)

Cree otro repositorio con el proyecto clonado, pero tras intentarlo enlazar a firebase, el proyecto no podía ejecutarse. Cada vez que lo intentaba, el ordenador se bloqueaba y tenía que reiniciarlo.

Por lo tanto cree otro proyecto desde 0 con implementaciones muy básicas pero que cumple con el objetivo de la práctica, que es el de implementar firebase y que las novelas se guarden adecuadamente y se puedan añadir.

Seguiré tratando de recuperar lo que hice para la primera práctica pero de momento para la fecha de hoy he tenido que hacer este repositorio, en cuanto consiga enlazar la primera práctica con firebase y que mi ordenador pueda ejecutarla adecuadamente modificaré el README de esta práctica con el enlace del repositorio arreglado.

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
