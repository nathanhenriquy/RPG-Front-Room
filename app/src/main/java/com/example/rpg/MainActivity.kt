package com.example.rpg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import atributos.Atributos
import classe.Guerreiro
import classe.Ladrao
import classe.Mago
import classe.Classe
import raca.Anao
import raca.Elfo
import raca.Halfling
import raca.Humano
import raca.Raca
import com.example.rpg.ui.theme.RPGTheme
import kotlin.random.Random
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.rpg.data.*

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RPGTheme {

                CharacterCreationScreen()
            }
        }
    }
}

// ============ LOGICA DOS DADOS
private fun rolarDado(lados: Int = 6): Int = Random.nextInt(1, lados + 1)
private fun rolar3d6(): Int = (1..3).sumOf { rolarDado(6) }
private fun rolar4d6TirarMenor(): Int {
    val result = List(4) { rolarDado(6) }
    return result.sortedDescending().take(3).sum()}

private fun gerarValoresAventureiro(): List<Int> = List(6) { rolar3d6() }
private fun gerarValoresHeroico(): List<Int> = List(6) { rolar4d6TirarMenor() }


// ============= TELAS

@Composable
fun CharacterCreationScreen(
    // Pega uma instância do ViewModel
    viewModel: PersonagemViewModel = viewModel()
) {

    var etapa by remember { mutableStateOf(0) } // qual tela sera mostrada

    var nome by remember { mutableStateOf("") }
    val atributos = remember {
        mutableStateMapOf(
            "Força" to 0,
            "Destreza" to 0,
            "Constituição" to 0,
            "Inteligência" to 0,
            "Sabedoria" to 0,
            "Carisma" to 0
        )
    }
    val availableNumbers = remember { mutableStateListOf<Int>() }
    var selectedNumber by remember { mutableStateOf<Int?>(null) }
    val classes = listOf(Guerreiro(), Ladrao(), Mago())
    val races = listOf(Anao(), Elfo(), Halfling(), Humano())
    var selectedClass by remember { mutableStateOf<Classe?>(null) }
    var selectedRace by remember { mutableStateOf<Raca?>(null) }

    val listaPersonagens by viewModel.personagens.collectAsStateWithLifecycle()


    Column(modifier = Modifier.fillMaxSize()) {
        when (etapa) { // troca de telas feita pelo when

            0 -> {
                // TELA: Nome
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Nome do Personagem", style = MaterialTheme.typography.headlineSmall)
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = nome,
                        onValueChange = { nome = it },
                        label = { Text("Nome") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Spacer(Modifier.weight(1f))
                        Button(onClick = { if (nome.isNotBlank()) etapa = 1 }) {
                            Text("Próximo")
                        }
                    }


                    // Exibe a lista de personagens já salvos
                    Spacer(Modifier.height(24.dp))
                    Text("Personagens Salvos", style = MaterialTheme.typography.headlineSmall)
                    Spacer(Modifier.height(12.dp))

                    if (listaPersonagens.isEmpty()) {
                        Text("Nenhum personagem salvo.")
                    } else {
                        LazyColumn(modifier = Modifier.weight(1f)) {
                            items(listaPersonagens) { p ->
                                Card(modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text("${p.nome} - ${p.raca} ${p.classe}", fontWeight = FontWeight.Bold)
                                        Text("FOR:${p.forca} DES:${p.destreza} CON:${p.constituicao} INT:${p.inteligencia} SAB:${p.sabedoria} CAR:${p.carisma}")

                                        // Botão para deletar
                                        OutlinedButton(
                                            onClick = { viewModel.deletar(p) },
                                            modifier = Modifier.padding(top = 8.dp)
                                        ) {
                                            Text("Deletar")
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }

            1 -> {
                // TELA: Escolha do metodo
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())) {
                    Text("Método de Atributos", style = MaterialTheme.typography.headlineSmall)
                    Spacer(Modifier.height(12.dp))

                    // Botão Clássico
                    Button(onClick = {
                        val gerado = Atributos.gerarMetodoClassico()
                        // aplica gerado aos atributos
                        atributos["Força"] = gerado.forca
                        atributos["Destreza"] = gerado.destreza
                        atributos["Constituição"] = gerado.constituicao
                        atributos["Inteligência"] = gerado.inteligencia
                        atributos["Sabedoria"] = gerado.sabedoria
                        atributos["Carisma"] = gerado.carisma
                        // limpa numeros disponíveis
                        availableNumbers.clear()
                        selectedNumber = null
                        etapa = 2
                    }, modifier = Modifier.fillMaxWidth()) {
                        Text("Clássico (3d6 — valores atribuídos automaticamente)")
                    }
                    Spacer(Modifier.height(8.dp))

                    // Botão Aventureiro: gera 6x 3d6 e permite distribuir
                    Button(onClick = {
                        val vals = gerarValoresAventureiro().sortedDescending()
                        availableNumbers.clear()
                        availableNumbers.addAll(vals)
                        // zera os atributos para distribuição
                        atributos.keys.forEach { atributos[it] = 0 }
                        selectedNumber = null
                        etapa = 2
                    }, modifier = Modifier.fillMaxWidth()) {
                        Text("Aventureiro (3d6 — sorteie valores e distribua)")
                    }
                    Spacer(Modifier.height(8.dp))

                    // Botão Heróico: gera 6x 4d6 drop lowest e distribui
                    Button(onClick = {
                        val vals = gerarValoresHeroico().sortedDescending()
                        availableNumbers.clear()
                        availableNumbers.addAll(vals)
                        atributos.keys.forEach { atributos[it] = 0 }
                        selectedNumber = null
                        etapa = 2
                    }, modifier = Modifier.fillMaxWidth()) {
                        Text("Heróico (4d6, remove menor — sorteie e distribua)")
                    }
                }
            }

            2 -> {
                // TELA: Atributos (se clássico já tem valores; se distribuível, user faz atribuição)
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
                ) {
                    Text("Atributos", style = MaterialTheme.typography.headlineSmall)
                    Spacer(Modifier.height(10.dp))

                    // Se há numeros disponíveis mostra blocos para distribuição
                    if (availableNumbers.isNotEmpty()) {
                        Text("Valores disponíveis:")
                        Spacer(Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                            availableNumbers.forEach { n ->
                                val isSelected = selectedNumber == n
                                OutlinedButton(
                                    onClick = { selectedNumber = if (isSelected) null else n },
                                    modifier = Modifier.padding(end = 8.dp)
                                ) {
                                    Text(text = "$n" + if (isSelected) " ✓" else "")
                                }
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                    } else {
                        Text("Nenhum número disponível ")
                        Spacer(Modifier.height(8.dp))
                    }

                    // Lista de atributos
                    atributos.forEach { (nomeAtrib, valor) ->
                        val hasValue = valor > 0
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .clickable {
                                    if (selectedNumber != null) {
                                        // atribuir selectedNumber ao atributo (se já tiver valor, devolve o antigo)
                                        val prev = atributos[nomeAtrib] ?: 0
                                        if (prev > 0) {
                                            // devolve prev para a fila de disponíveis
                                            availableNumbers.add(prev)
                                        }
                                        atributos[nomeAtrib] = selectedNumber!!
                                        // remove uma ocorrência do número escolhido
                                        availableNumbers.remove(selectedNumber)
                                        selectedNumber = null
                                    } else {
                                        // se não escolheu número, clique em atributo retira o valor (devolve para a lista)
                                        val prev = atributos[nomeAtrib] ?: 0
                                        if (prev > 0) {
                                            atributos[nomeAtrib] = 0
                                            availableNumbers.add(prev)
                                        }
                                    }
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = if (hasValue) Color(0xFFE8F5E9) else MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(nomeAtrib, fontWeight = FontWeight.Medium)
                                Text((atributos[nomeAtrib] ?: 0).toString(), fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    // botão próximo só aparece quando não há números pendentes
                    val allAssigned = atributos.values.all { it > 0 } || availableNumbers.isEmpty()
                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(onClick = {
                            // voltar para métodos caso queira refazer
                            availableNumbers.clear()
                            selectedNumber = null
                            etapa = 1
                        }) { Text("Voltar") }

                        Spacer(Modifier.weight(1f))

                        Button(onClick = { if (allAssigned) etapa = 3 }) {
                            Text("Próximo")
                        }
                    }
                }
            }

            3 -> {
                // TELA: Escolha de Classe
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
                ) {
                    Text("Escolha a Classe", style = MaterialTheme.typography.headlineSmall)
                    Spacer(Modifier.height(8.dp))
                    classes.forEach { c ->
                        val selected = selectedClass?.nome == c.nome
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .clickable { selectedClass = c },
                            colors = CardDefaults.cardColors(
                                containerColor = if (selected) Color(0xFFBBDEFB) else MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(c.nome, fontWeight = FontWeight.Bold)
                                Spacer(Modifier.height(6.dp))
                                Text("Armas: ${c.restricoesDeArmas}")
                                Text("Armaduras: ${c.restricoesDeArmaduras}")
                                Spacer(Modifier.height(6.dp))
                                Text("Habilidades:", fontWeight = FontWeight.Medium)
                                c.habilidades.forEach { h -> Text("- $h") }
                            }
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(onClick = { etapa = 2 }) { Text("Voltar") }
                        Spacer(Modifier.weight(1f))
                        Button(onClick = { if (selectedClass != null) etapa = 4 }) { Text("Próximo") }
                    }
                }
            }

            4 -> {
                // TELA: Escolha de Raça
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
                ) {
                    Text("Escolha a Raça", style = MaterialTheme.typography.headlineSmall)
                    Spacer(Modifier.height(8.dp))
                    races.forEach { r ->
                        val selected = selectedRace?.nome == r.nome
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .clickable { selectedRace = r },
                            colors = CardDefaults.cardColors(
                                containerColor = if (selected) Color(0xFFC8E6C9) else MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(r.nome, fontWeight = FontWeight.Bold)
                                Spacer(Modifier.height(6.dp))
                                Text("Movimento: ${r.movimento}  •  Infravisão: ${r.infravisao}")
                                Text("Alinhamento: ${r.alinhamento}")
                                Spacer(Modifier.height(6.dp))
                                Text("Habilidades:", fontWeight = FontWeight.Medium)
                                r.habilidades().forEach { h -> Text("- $h") }
                            }
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(onClick = { etapa = 3 }) { Text("Voltar") }
                        Spacer(Modifier.weight(1f))
                        Button(onClick = { if (selectedRace != null) etapa = 5 }) { Text("Finalizar") }
                    }
                }
            }

            5 -> {
                // TELA: Resumo final
                LazyColumn(modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                ) {
                    item {
                        Text("Resumo do Personagem", style = MaterialTheme.typography.headlineSmall)
                        Spacer(Modifier.height(12.dp))
                    }

                    item {
                        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("Nome", fontWeight = FontWeight.Bold)
                                Text(nome.ifBlank { "—" })
                            }
                        }
                    }

                    item {
                        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("Atributos", fontWeight = FontWeight.Bold)
                                Spacer(Modifier.height(6.dp))
                                atributos.forEach { (k, v) ->
                                    Text("$k: $v")
                                }
                            }
                        }
                    }

                    selectedClass?.let { c ->
                        item {
                            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text("Classe: ${c.nome}", fontWeight = FontWeight.Bold)
                                    Spacer(Modifier.height(6.dp))
                                    Text("Armas: ${c.restricoesDeArmas}")
                                    Text("Armaduras: ${c.restricoesDeArmaduras}")
                                    Spacer(Modifier.height(6.dp))
                                    Text("Habilidades:", fontWeight = FontWeight.Medium)
                                    c.habilidades.forEach { h -> Text("- $h") }
                                }
                            }
                        }
                    }

                    selectedRace?.let { r ->
                        item {
                            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text("Raça: ${r.nome}", fontWeight = FontWeight.Bold)
                                    Spacer(Modifier.height(6.dp))
                                    Text("Movimento: ${r.movimento}  •  Infravisão: ${r.infravisao}")
                                    Text("Alinhamento: ${r.alinhamento}")
                                    Spacer(Modifier.height(6.dp))
                                    Text("Habilidades:", fontWeight = FontWeight.Medium)
                                    r.habilidades().forEach { h -> Text("- $h") }
                                }
                            }
                        }
                    }

                    item {
                        Spacer(Modifier.height(12.dp))
                        Row(modifier = Modifier.fillMaxWidth()) {
                            OutlinedButton(onClick = {
                                // volta pra escolha de raça para editar
                                etapa = 4
                            }) { Text("Voltar") }

                            Spacer(Modifier.weight(1f))


                            // Botão de Salvar
                            Button(onClick = {

                                val novoPersonagem = Personagem(
                                    nome = nome,
                                    classe = selectedClass?.nome ?: "N/A",
                                    raca = selectedRace?.nome ?: "N/A",
                                    forca = atributos["Força"] ?: 0,
                                    destreza = atributos["Destreza"] ?: 0,
                                    constituicao = atributos["Constituição"] ?: 0,
                                    inteligencia = atributos["Inteligência"] ?: 0,
                                    sabedoria = atributos["Sabedoria"] ?: 0,
                                    carisma = atributos["Carisma"] ?: 0
                                )

                                viewModel.inserir(novoPersonagem)


                                nome = ""
                                atributos.keys.forEach { atributos[it] = 0 }
                                availableNumbers.clear()
                                selectedNumber = null
                                selectedClass = null
                                selectedRace = null
                                etapa = 0 // Volta para a tela inicial
                            }) {
                                Text("Salvar e Voltar ao Início")
                            }
                        }
                    }
                }
            }
        }
    }
}