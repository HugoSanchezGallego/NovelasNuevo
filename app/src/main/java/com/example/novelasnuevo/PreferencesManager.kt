package com.example.novelasnuevo

import android.content.Context
import android.content.SharedPreferences

object PreferencesManager {
    private const val PREFS_NAME = "user_prefs"
    private const val THEME_KEY_PREFIX = "theme_"

    fun saveTheme(context: Context, username: String, isDarkTheme: Boolean) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(THEME_KEY_PREFIX + username, isDarkTheme)
        editor.apply()
    }

    fun isDarkTheme(context: Context, username: String): Boolean {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(THEME_KEY_PREFIX + username, false)
    }
}