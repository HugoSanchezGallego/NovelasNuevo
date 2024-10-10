package com.example.novelasnuevo

data class Novela(
    val id: Int = 0,
    val titulo: String = "",
    val autor: String = "",
    val anoPublicacion: Int = 0,
    val sinopsis: String = "",
    var esFavorita: Boolean = false,
    val resenas: MutableList<String> = mutableListOf()
)