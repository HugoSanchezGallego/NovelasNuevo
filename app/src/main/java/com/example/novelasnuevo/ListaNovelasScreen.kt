package com.example.novelasnuevo

import Novela
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ListaNovelasScreen(novelas: List<Novela>, onNovelaClick: (Novela) -> Unit) {
    var selectedNovela by remember { mutableStateOf<Novela?>(null) }
    val db = FirebaseFirestore.getInstance()

    LazyColumn {
        items(novelas) { novela ->
            NovelaItem(novela) {
                selectedNovela = novela
            }
        }
    }

    selectedNovela?.let { novela ->
        NovelaDetailsDialog(
            novela = novela,
            onDismiss = { selectedNovela = null },
            onMarcarFavorita = { novela ->
                val newFavoritaStatus = !novela.esFavorita
                db.collection("novelas").document(novela.id.toString())
                    .update("esFavorita", newFavoritaStatus)
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
                        selectedNovela = null
                    }
            }
        )
    }
}

@Composable
fun NovelaItem(novela: Novela, onClick: () -> Unit) {
    val backgroundColor = if (novela.esFavorita) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = novela.titulo, style = MaterialTheme.typography.titleMedium)
            Text(text = "Autor: ${novela.autor}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Año: ${novela.anoPublicacion}", style = MaterialTheme.typography.bodyMedium)
            if (novela.esFavorita) {
                Icon(imageVector = Icons.Default.Favorite, contentDescription = "Favorita")
            }
        }
    }
}

@Composable
fun NovelaDetailsDialog(
    novela: Novela,
    onDismiss: () -> Unit,
    onMarcarFavorita: (Novela) -> Unit,
    onEliminarNovela: (Novela) -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    var isFavorita by remember { mutableStateOf(novela.esFavorita) }
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = novela.titulo) },
        text = {
            Column {
                Text(text = "Autor: ${novela.autor}", style = MaterialTheme.typography.bodyLarge)
                Text(text = "Año: ${novela.anoPublicacion}", style = MaterialTheme.typography.bodyLarge)
                Text(text = "Sinopsis: ${novela.sinopsis}", style = MaterialTheme.typography.bodyLarge)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isFavorita,
                        onCheckedChange = { checked ->
                            isFavorita = checked
                            val updatedNovela = novela.copy(esFavorita = checked)
                            db.collection("novelas").document(novela.id.toString())
                                .update("esFavorita", checked)
                                .addOnSuccessListener {
                                    onMarcarFavorita(updatedNovela)
                                }
                        }
                    )
                    Text(text = if (isFavorita) "Favorita" else "No Favorita")
                }
                novela.latitude?.let { lat ->
                    novela.longitude?.let { lon ->
                        Button(onClick = {
                            val uri = "geo:0,0?q=$lat,$lon"
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                            intent.setPackage("com.google.android.apps.maps")
                            context.startActivity(intent)
                        }) {
                            Text("Ver Ubicación")
                        }
                    }
                }
            }
        },
        confirmButton = {
            Column {
                Button(onClick = { onEliminarNovela(novela) }) {
                    Text("Eliminar Novela")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onDismiss) {
                    Text("Cerrar")
                }
            }
        }
    )
}