package com.example.novelasnuevo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite

@Composable
fun ListaNovelasScreen(novelas: List<Novela>, onNovelaClick: (Novela) -> Unit) {
    LazyColumn {
        items(novelas) { novela ->
            NovelaItem(novela, onNovelaClick)
        }
    }
}

@Composable
fun NovelaItem(novela: Novela, onNovelaClick: (Novela) -> Unit) {
    val backgroundColor = if (novela.esFavorita) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onNovelaClick(novela) },
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