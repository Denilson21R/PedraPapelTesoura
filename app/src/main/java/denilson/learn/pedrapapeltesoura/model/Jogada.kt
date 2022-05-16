package denilson.learn.pedrapapeltesoura.model

enum class Jogada {
    PEDRA, PAPEL, TESOURA;

    override fun toString(): String {
        return this.name.lowercase().replaceFirstChar { it.uppercase() }
    }
}