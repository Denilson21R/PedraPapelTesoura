package denilson.learn.pedrapapeltesoura.DAO

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import denilson.learn.pedrapapeltesoura.R
import denilson.learn.pedrapapeltesoura.model.Configuracao
import java.sql.SQLException

class ConfigSQLite(contexto: Context) : ConfigDAO {

    companion object {
        private val BD_CONFIG = "configuracao"
        private val TABELA_CONFIG = "configuracao"
        private val COLUNA_CONFIG = "configuracao"

        val CRIAR_TABELA_CONFIG_STMT = "CREATE TABLE IF NOT EXISTS ${TABELA_CONFIG} (" +
                "${COLUNA_CONFIG} INTEGER);"
    }

    private val configBd: SQLiteDatabase

    init{
        configBd = contexto.openOrCreateDatabase(BD_CONFIG, Context.MODE_PRIVATE, null)

        try{
            configBd.execSQL(CRIAR_TABELA_CONFIG_STMT)
        }
        catch (se: SQLException) {
            Log.e(contexto.getString(R.string.app_name), se.toString())
        }
    }

    override fun getConfig(): Configuracao? {
        val query = "SELECT ${COLUNA_CONFIG} FROM ${TABELA_CONFIG}"
        val cursor = configBd.rawQuery(query,null)

        if (cursor.moveToFirst()){
            val result = Configuracao()
            result.numeroAdversarios = cursor.getInt(cursor.getColumnIndex(COLUNA_CONFIG))
            return result
        }

        return null;
    }

    override fun adicionarConfig(config: Configuracao): Long {
        val configCv = ContentValues()
        configCv.put(COLUNA_CONFIG, config.numeroAdversarios)

        return configBd.insert(TABELA_CONFIG, null, configCv)
    }

    override fun alterarConfig(config: Configuracao) : Int {
        val configCv = ContentValues()
        configCv.put(COLUNA_CONFIG,config.numeroAdversarios)
        val whereClause = "${COLUNA_CONFIG} IS NOT NULL";
        return configBd.update(TABELA_CONFIG,configCv, whereClause, null)
    }
}