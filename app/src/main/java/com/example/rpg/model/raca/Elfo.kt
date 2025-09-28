package raca

class Elfo : Raca("Elfo", 9, 18, "Tendem a Neutralidade") {
    override fun habilidades() = listOf (
        "Percepção Natural (Detecta portas secretas)",
        "Graciosos (+1 em testes de JPD)",
        "Arma Racial (+1 nos danos causados com arcos)",
        "Imunidades (imunes a efeitos ou magias que envolvam sonos e paralisia causada por Ghoul)"
    )

}