package classe

class Ladrao : Classe {
    override val nome = "Ladrão"
    override val restricoesDeArmas = "Pode usar apenas armas leves "
    override val restricoesDeArmaduras = "Pode usar apenas armaduras leves (couro)"
    override val habilidades = listOf(
        "Furtividade",
        "Desarmar armadilhas",
        "Abrir fechaduras",
        "Roubo e acrobacias"
    )
}
