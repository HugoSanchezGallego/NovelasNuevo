package com.example.novelasnuevo

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun AuthScreen(onAuthSuccess: (String) -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLogin by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val db = FirebaseFirestore.getInstance()

    MaterialTheme(colorScheme = lightColorScheme()) {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = if (isLogin) "Iniciar Sesión" else "Registrarse", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Nombre de usuario") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        if (isLogin) {
                            db.collection("users").document(username).get()
                                .addOnSuccessListener { document ->
                                    if (document.exists() && document.getString("password") == password) {
                                        onAuthSuccess(username)
                                    } else {
                                        errorMessage = "Nombre de usuario o contraseña inválidos"
                                    }
                                }
                        } else {
                            db.collection("users").document(username).get()
                                .addOnSuccessListener { document ->
                                    if (document.exists()) {
                                        errorMessage = "El nombre de usuario ya existe"
                                    } else {
                                        val user = hashMapOf(
                                            "username" to username,
                                            "password" to password
                                        )
                                        db.collection("users").document(username).set(user)
                                            .addOnSuccessListener {
                                                onAuthSuccess(username)
                                            }
                                            .addOnFailureListener { e ->
                                                errorMessage = e.message
                                            }
                                    }
                                }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = if (isLogin) "Iniciar Sesión" else "Registrarse")
                }
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(onClick = { isLogin = !isLogin }) {
                    Text(text = if (isLogin) "¿No tienes una cuenta? Regístrate" else "¿Ya tienes una cuenta? Inicia sesión")
                }
                errorMessage?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}