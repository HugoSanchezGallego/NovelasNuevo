package com.example.novelasnuevo

import android.app.Activity
import android.content.Context
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
import android.content.Intent

// MainActivity.kt
class MainActivity : ComponentActivity() {
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        db = FirebaseFirestore.getInstance()

        setContent {
            var authenticatedUser by remember { mutableStateOf<Pair<String, String>?>(null) }
            var isDarkTheme by remember { mutableStateOf(false) }
            var showSettings by remember { mutableStateOf(false) }

            authenticatedUser?.let { (username, _) ->
                isDarkTheme = PreferencesManager.isDarkTheme(LocalContext.current, username)
            }

            NovelasNuevoTheme(darkTheme = isDarkTheme) {
                Surface(color = MaterialTheme.colorScheme.background) {
                    if (showSettings) {
                        SettingsScreen(
                            username = authenticatedUser!!.first,
                            isDarkTheme = isDarkTheme,
                            onBack = { showSettings = false },
                            onThemeChange = { isDarkTheme = it }
                        )
                    } else if (authenticatedUser != null) {
                        MainScreen(
                            db = db,
                            username = authenticatedUser!!.first,
                            password = authenticatedUser!!.second,
                            onSettingsClick = { showSettings = true },
                            onLogoutClick = { authenticatedUser = null },
                            registerUser = ::registerUser
                        )
                    } else {
                        AuthScreen(onAuthSuccess = { username, password -> authenticatedUser = username to password })
                    }
                }
            }
        }
    }

    fun registerUser(context: Context, username: String, password: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val randomLatitude = 36.0 + Math.random() * (43.0 - 36.0) // Latitude range for Spain
        val randomLongitude = -9.0 + Math.random() * (3.0 - (-9.0)) // Longitude range for Spain

        val user = hashMapOf(
            "username" to username,
            "password" to password,
            "latitude" to randomLatitude,
            "longitude" to randomLongitude
        )
        db.collection("users").document(username).set(user)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onFailure(e.message ?: "Error al registrar el usuario")
            }
    }
}

@Composable
fun MainScreen(
    db: FirebaseFirestore,
    username: String,
    password: String,
    onSettingsClick: () -> Unit,
    onLogoutClick: () -> Unit,
    registerUser: (Context, String, String, () -> Unit, (String) -> Unit) -> Unit
) {
    var novelas by remember { mutableStateOf<List<Novela>>(emptyList()) }
    var selectedNovela by remember { mutableStateOf<Novela?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(username) {
        registerUser(context, username, password, {
            println("User registered successfully")
        }, { error ->
            println("Failed to register user: $error")
        })

        db.collection("novelas")
            .whereEqualTo("username", username)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    novelas = snapshot.toObjects(Novela::class.java).sortedByDescending { it.esFavorita }
                }
            }
    }

    if (selectedNovela == null) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            ListaNovelasScreen(novelas) { novela ->
                selectedNovela = novela
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Button(onClick = { showDialog = true }) {
                    Text("Añadir Novela")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = onLogoutClick) {
                    Text("Cerrar Sesión")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Button(onClick = onSettingsClick) {
                    Text("Ajustes")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = { showCurrentLocation(context, username) }) {
                    Text("Mostrar Ubicación")
                }
            }
        }

        if (showDialog) {
            AddNovelaDialog(
                onDismiss = { showDialog = false },
                onAdd = { nuevaNovela ->
                    db.collection("novelas").add(nuevaNovela.copy(username = username))
                    showDialog = false
                },
                onVolver = { showDialog = false }
            )
        }
    } else {
        NovelaDetailsDialog(
            novela = selectedNovela!!,
            onDismiss = { selectedNovela = null },
            onMarcarFavorita = { updatedNovela ->
                db.collection("novelas").document(updatedNovela.id.toString())
                    .update("esFavorita", updatedNovela.esFavorita)
                    .addOnSuccessListener {
                        selectedNovela = null
                    }
            },
            onEliminarNovela = { novela ->
                db.collection("novelas")
                    .whereEqualTo("id", novela.id)
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            db.collection("novelas").document(document.id).delete()
                        }
                        novelas = novelas.filter { it.id != novela.id }
                        selectedNovela = null
                    }
            }
        )
    }
}

fun showCurrentLocation(context: Context, username: String) {
    val db = FirebaseFirestore.getInstance()
    db.collection("users").document(username).get()
        .addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                val latitude = document.getDouble("latitude")
                val longitude = document.getDouble("longitude")
                if (latitude != null && longitude != null) {
                    val uri = "geo:$latitude,$longitude?q=$latitude,$longitude"
                    val intent = Intent(Intent.ACTION_VIEW, android.net.Uri.parse(uri))
                    context.startActivity(intent)
                } else {
                    println("Location data is missing")
                }
            } else {
                println("User document does not exist")
            }
        }
        .addOnFailureListener { e ->
            println("Failed to fetch user location: ${e.message}")
        }
}