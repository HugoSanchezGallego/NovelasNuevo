package com.example.novelasnuevo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.example.novelasnuevo.ui.theme.NovelasNuevoTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this) // Initialize Firebase
        db = FirebaseFirestore.getInstance() // Get Firestore instance

        setContent {
            NovelasNuevoTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    var novelas by remember { mutableStateOf<List<Novela>>(emptyList()) }
                    var selectedNovela by remember { mutableStateOf<Novela?>(null) }
                    var showDialog by remember { mutableStateOf(false) }

                    LaunchedEffect(Unit) {
                        db.collection("novelas").addSnapshotListener { snapshot, _ ->
                            if (snapshot != null) {
                                novelas = snapshot.toObjects(Novela::class.java)
                            }
                        }
                    }

                    if (selectedNovela == null) {
                        ListaNovelasScreen(novelas) { novela ->
                            selectedNovela = novela
                        }

                        Button(onClick = { showDialog = true }) {
                            Text("Añadir Novela")
                        }

                        if (showDialog) {
                            AddNovelaDialog(
                                onDismiss = { showDialog = false },
                                onAdd = { nuevaNovela ->
                                    db.collection("novelas").add(nuevaNovela)
                                    showDialog = false
                                }
                            )
                        }
                    } else {
                        DetallesNovelaScreen(
                            novela = selectedNovela!!,
                            onMarcarFavorita = { novela ->
                                db.collection("novelas").document(novela.id.toString())
                                    .update("esFavorita", !novela.esFavorita)
                            },
                            onEliminarNovela = { novela ->
                                db.collection("novelas").document(novela.id.toString()).delete()
                                selectedNovela = null
                            }
                        )
                    }
                }
            }
        }
    }
}