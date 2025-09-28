package atributos
import kotlin.random.Random


data class Atributos(
    val forca: Int,
    val destreza: Int,
    val constituicao: Int,
    val inteligencia: Int,
    val sabedoria: Int,
    val carisma: Int
) {
    companion object {
        private fun rolarDado(lados: Int) = Random.nextInt(1, lados + 1)
        // roda do numero 1 a quantidade de lados e retorna o numero

        private fun rolar3d6() = (1..3).sumOf { rolarDado(6) }
        // roda 3 vzs o dado e faz a soma

        private fun rolar4d6TirarMenor(): Int {
            val result = List(4) { rolarDado(6) }
            return result.sortedDescending().take(3).sum()
            // descarta o menor valor dos 4 numeros e soma os 3 maiores
        }

        fun gerarMetodoClassico(): Atributos {
            println("Metodo Classico (3d6, na ordem)")
            val valores = List(6) { rolar3d6() }
            return Atributos(valores[0], valores[1], valores[2], valores[3], valores[4], valores[5])
        }

        fun gerarMetodoAventureiro(): Atributos {
            println("Metodo Aventureiro (3d6, o usuario distribui)")
            val valores = List(6) { rolar3d6() }
            println("Valores rolados: $valores")

            return distribuir(valores)
        }

        fun gerarMetodoHeroico(): Atributos {
            println("Metodo Heroico (4d6, remove menor, e usuario distribui)")
            val valores = List(6) { rolar4d6TirarMenor() }
            println("Valores rolados: $valores")

            return distribuir(valores)
        }


        private fun distribuir(valores: List<Int>): Atributos {
            val atributosNomes = listOf("Força", "Destreza", "Constituição", "Inteligência", "Sabedoria", "Carisma")
            val valoresDisponiveis = valores.toMutableList()
            val escolhidos = mutableListOf<Int>()

            for (atributo in atributosNomes) {
                println("Escolha um valor para $atributo (disponíveis: $valoresDisponiveis): ")
                val escolha = readLine()?.toIntOrNull()

                if (escolha != null && valoresDisponiveis.contains(escolha)) {
                    escolhidos.add(escolha)
                    valoresDisponiveis.remove(escolha)
                } else {
                    println("Escolha inválida, será usado automaticamente o primeiro valor disponível.")
                    escolhidos.add(valoresDisponiveis.removeAt(0))
                }
            }

            return Atributos(escolhidos[0], escolhidos[1], escolhidos[2], escolhidos[3], escolhidos[4], escolhidos[5])
        }
    }









}