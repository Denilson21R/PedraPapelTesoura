package denilson.learn.pedrapapeltesoura.DAO

import denilson.learn.pedrapapeltesoura.model.Configuracao

interface ConfigDAO {
    fun getConfig() : Configuracao?
    fun adicionarConfig(config: Configuracao) : Long
    fun alterarConfig(config: Configuracao): Int
}