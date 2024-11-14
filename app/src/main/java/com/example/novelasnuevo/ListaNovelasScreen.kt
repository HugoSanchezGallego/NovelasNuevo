package com.example.novelasnuevo

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

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = novela.titulo) },
        text = {
            Column {
                Text(text = "Autor: ${novela.autor}", style = MaterialTheme.typography.bodyLarge)
                Text(text = "Año: ${novela.anoPublicacion}", style = MaterialTheme.typography.bodyLarge)
                Text(text = "Sinopsis: ${novela.sinopsis}", style = MaterialTheme.typography.bodyLarge)
            }
        },
        confirmButton = {
            Column {
                Button(onClick = {
                    val newFavoritaStatus = !novela.esFavorita
                    db.collection("novelas").document(novela.id.toString())
                        .update("esFavorita", newFavoritaStatus)
                        .addOnSuccessListener {
                            onMarcarFavorita(novela.copy(esFavorita = newFavoritaStatus))
                        }
                }) {
                    Text(text = if (novela.esFavorita) "Desmarcar Favorita" else "Marcar Favorita")
                }
                Spacer(modifier = Modifier.height(8.dp))
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