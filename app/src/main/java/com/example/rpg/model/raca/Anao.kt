package raca

class Anao : Raca ("Anao", 6, 18, "Tendem a ordem") {
    override fun habilidades()= listOf (
        "Mineradores (detectam armadilhas em pedras)",
        "Vigorosos (são resistentes, +1 em qualquer teste de JPC)",
        "Armas grandes (não usam armas grandes, apeas médias e pequenas)",
        "Inimigos (Orcs, Ogros, HobGoblins)"
    )

}