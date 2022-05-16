package denilson.learn.pedrapapeltesoura

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import denilson.learn.pedrapapeltesoura.databinding.ActivityConfigBinding
import denilson.learn.pedrapapeltesoura.model.Configuracao

class ConfigActivity : AppCompatActivity() {
    private lateinit var activityConfigBinding : ActivityConfigBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityConfigBinding = ActivityConfigBinding.inflate(layoutInflater)
        setContentView(activityConfigBinding.root)

        supportActionBar?.title = "Configurações"

        val configAtual = intent.extras?.getParcelable<Configuracao>(Intent.EXTRA_USER)
        if (configAtual != null){
            activityConfigBinding.qtddJogadoresSp.setSelection(configAtual.numeroAdversarios - 1)
        }

        activityConfigBinding.salvarConfigBtn.setOnClickListener {
            val novaConfig = Configuracao(
                activityConfigBinding.qtddJogadoresSp.selectedItem.toString().toInt()
            )
            val retornIntent = Intent();
            retornIntent.putExtra(Intent.EXTRA_USER, novaConfig)
            setResult(RESULT_OK, retornIntent)
            finish()
        }
    }
}