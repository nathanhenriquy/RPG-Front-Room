package raca

abstract class Raca(
    val nome: String,
    val movimento: Int,
    val infravisao: Int,
    val alinhamento: String
) {
    abstract fun habilidades(): List<String>
}
