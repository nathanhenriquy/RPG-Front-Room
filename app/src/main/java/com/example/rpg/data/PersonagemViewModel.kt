package com.example.rpg.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PersonagemViewModel(application: Application) : AndroidViewModel(application) {

    // Pega  do Database
    private val dao: PersonagemDao = AppDatabase.getDatabase(application).personagemDao()

    private val _personagens = MutableStateFlow<List<Personagem>>(emptyList())
    val personagens = _personagens.asStateFlow()

    init {
        // Carrega a lista
        carregarPersonagens()
    }

    private fun carregarPersonagens() {
        viewModelScope.launch {
            _personagens.value = dao.listarTodos()
        }
    }

    // salvar um personagem
    fun inserir(personagem: Personagem) {
        viewModelScope.launch {
            dao.inserir(personagem)
            carregarPersonagens()
        }
    }

    // deletar
    fun deletar(personagem: Personagem) {
        viewModelScope.launch {
            dao.deletar(personagem)
            carregarPersonagens()
        }
    }
}