package com.example.novelasnuevo

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(username: String, isDarkTheme: Boolean, onBack: () -> Unit, onThemeChange: (Boolean) -> Unit) {
    val context = LocalContext.current
    var isDarkThemeState by remember { mutableStateOf(isDarkTheme) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Settings", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Dark Theme")
            Switch(
                checked = isDarkThemeState,
                onCheckedChange = {
                    isDarkThemeState = it
                    PreferencesManager.saveTheme(context, username, it)
                    onThemeChange(it)
                }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBack) {
            Text("Back")
        }
    }
}