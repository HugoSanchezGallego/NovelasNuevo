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

## Configuración del Proyecto

1. Clona el repositorio:
    ```sh
    git clone https://github.com/HugoSanchezGallego/NovelasNuevo.git
    cd NovelasNuevo
    ```

2. Abre el proyecto en Android Studio.

3. Configura Firebase:
    - Añade el archivo `google-services.json` en la carpeta `app`.
    - Asegúrate de tener las dependencias de Firebase en el archivo `build.gradle.kts`.

4. Sincroniza el proyecto con Gradle.

## Uso

1. Ejecuta la aplicación en un dispositivo o emulador Android.
2. Añade nuevas novelas utilizando el botón "Añadir Novela".
3. Elimina novelas seleccionando una novela y pulsando el botón "Eliminar Novela".

## Estructura del Proyecto

- `MainActivity.kt`: Actividad principal que maneja la lógica de la aplicación.
- `Novela.kt`: Data class que representa una novela.
- `DetallesNovelaScreen.kt`: Composable que muestra los detalles de una novela.
- `AddNovelaDialog.kt`: Composable que permite añadir una nueva novela.

## Contribuciones

Las contribuciones son bienvenidas. Por favor, abre un issue o un pull request para discutir cualquier cambio que desees realizar.

## Licencia

Este proyecto está licenciado bajo la Licencia MIT. Consulta el archivo `LICENSE` para más detalles.
