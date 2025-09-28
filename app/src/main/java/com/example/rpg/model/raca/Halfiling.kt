package raca

class Halfling : Raca("Halfling", 6, 0, "Tendem a Neutralidade") {
    override fun habilidades() = listOf(
        "Furtivos (chance de se esconder 1-2 em 1d6)",
        "Destemidos (+1 em JPS)",
        "Bons de Mira (ataques à distância mais fáceis)",
        "Pequenos (mais difíceis de serem acertados)"
    )
}