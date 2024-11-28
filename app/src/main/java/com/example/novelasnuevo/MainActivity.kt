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

@Composable
fun MainScreen(db: FirebaseFirestore, username: String, onSettingsClick: () -> Unit, onLogoutClick: () -> Unit) {
    var novelas by remember { mutableStateOf<List<Novela>>(emptyList()) }
    var selectedNovela by remember { mutableStateOf<Novela?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(username) {
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
            Button(onClick = onSettingsClick) {
                Text("Ajustes")
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