package com.example.novelasnuevo

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DetallesNovelaScreen(
    novela: Novela,
    onMarcarFavorita: (Novela) -> Unit,
    onEliminarNovela: (Novela) -> Unit,
    onVolver: () -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = novela.titulo, style = MaterialTheme.typography.headlineMedium)
        Text(text = "Autor: ${novela.autor}", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Año: ${novela.anoPublicacion}", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Sinopsis: ${novela.sinopsis}", style = MaterialTheme.typography.bodyLarge)
        Button(onClick = { onMarcarFavorita(novela) }) {
            Text(text = if (novela.esFavorita) "Desmarcar Favorita" else "Marcar Favorita")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onEliminarNovela(novela) }) {
            Text("Eliminar Novela")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onVolver) {
            Text("Volver a la Lista")
        }
    }
}