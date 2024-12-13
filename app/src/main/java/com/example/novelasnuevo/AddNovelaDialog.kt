package com.example.novelasnuevo

import Novela
import android.app.Activity
import android.location.Location
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun AddNovelaDialog(username: String, onDismiss: () -> Unit, onAdd: (Novela) -> Unit, onVolver: () -> Unit) {
    var titulo by remember { mutableStateOf("") }
    var autor by remember { mutableStateOf("") }
    var anoPublicacion by remember { mutableStateOf("") }
    var sinopsis by remember { mutableStateOf("") }
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Añadir Novela") },
        text = {
            Column {
                TextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título") }
                )
                TextField(
                    value = autor,
                    onValueChange = { autor = it },
                    label = { Text("Autor") }
                )
                TextField(
                    value = anoPublicacion,
                    onValueChange = { anoPublicacion = it },
                    label = { Text("Año de Publicación") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
                TextField(
                    value = sinopsis,
                    onValueChange = { sinopsis = it },
                    label = { Text("Sinopsis") }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = onVolver) {
                        Text("Volver a la Lista")
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                db.collection("users").document(username).get()
                    .addOnSuccessListener { document ->
                        val latitude = document.getDouble("latitude")
                        val longitude = document.getDouble("longitude")
                        if (latitude != null && longitude != null) {
                            val nuevaNovela = Novela(
                                id = (1..1000).random(),
                                titulo = titulo,
                                autor = autor,
                                anoPublicacion = anoPublicacion.toIntOrNull() ?: 0,
                                sinopsis = sinopsis,
                                username = username,
                                latitude = latitude,
                                longitude = longitude
                            )
                            onAdd(nuevaNovela)
                        } else {
                            // Handle location not available
                        }
                    }
            }) {
                Text("Añadir")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}