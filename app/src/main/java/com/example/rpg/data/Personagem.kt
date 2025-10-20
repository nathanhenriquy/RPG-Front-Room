package com.example.rpg.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "personagens")
data class Personagem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nome: String,
    val classe: String,
    val raca: String,
    val forca: Int,
    val destreza: Int,
    val constituicao: Int,
    val inteligencia: Int,
    val sabedoria: Int,
    val carisma: Int
)
