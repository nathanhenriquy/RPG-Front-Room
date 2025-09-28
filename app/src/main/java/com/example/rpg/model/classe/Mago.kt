package classe

class Mago : Classe {
    override val nome = "Mago"
    override val restricoesDeArmas = "Pode usar apenas cajados, adagas e dardos"
    override val restricoesDeArmaduras = "Usar escudos ou armaduras impede a conjuração de magias"
    override val habilidades = listOf(
        "Uso de magias arcanas",
        "Baixa resistência física"
    )
}
