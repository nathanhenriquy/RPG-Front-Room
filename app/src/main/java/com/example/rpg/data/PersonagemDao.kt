package com.example.rpg.data

import androidx.room.*

@Dao
interface PersonagemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(personagem: Personagem)

    @Query("SELECT * FROM personagens")
    suspend fun listarTodos(): List<Personagem>

    @Delete
    suspend fun deletar(personagem: Personagem)
}
