package raca

class Humano : Raca("Humano", 9, 0, "Qualquer") {
    override fun habilidades() = listOf (
            "Aprendizado rápido (+10% XP)",
            "Adaptabilidade (+1 em uma jogada de proteção)"
        )
}
