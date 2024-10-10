package com.example.novelasnuevo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun AddNovelaDialog(onDismiss: () -> Unit, onAdd: (Novela) -> Unit) {
    var titulo by remember { mutableStateOf("") }
    var autor by remember { mutableStateOf("") }
    var anoPublicacion by remember { mutableStateOf("") }
    var sinopsis by remember { mutableStateOf("") }

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
            }
        },
        confirmButton = {
            Button(onClick = {
                val nuevaNovela = Novela(
                    id = (1..1000).random(), // Generar un ID aleatorio
                    titulo = titulo,
                    autor = autor,
                    anoPublicacion = anoPublicacion.toIntOrNull() ?: 0,
                    sinopsis = sinopsis
                )
                onAdd(nuevaNovela)
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