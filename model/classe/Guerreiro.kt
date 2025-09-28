package classe

class Guerreiro : Classe {
    override val nome = "Guerreiro"
    override val restricoesDeArmas = "Nenhuma (pode usar todas as armas)"
    override val restricoesDeArmaduras = "Nenhuma (pode usar todas as armaduras)"
    override val habilidades = listOf(
        "Especialista em combate corpo a corpo",
        "Acesso a todas as armas e armaduras"
    )
}
