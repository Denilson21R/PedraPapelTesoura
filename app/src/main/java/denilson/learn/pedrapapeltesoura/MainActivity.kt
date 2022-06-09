package denilson.learn.pedrapapeltesoura

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import denilson.learn.pedrapapeltesoura.DAO.ConfigSQLite
import denilson.learn.pedrapapeltesoura.databinding.ActivityMainBinding
import denilson.learn.pedrapapeltesoura.model.Configuracao
import denilson.learn.pedrapapeltesoura.model.Jogada
import kotlin.random.Random
import kotlin.random.nextInt


class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding

    private lateinit var btn_pedra: ImageView
    private lateinit var btn_papel: ImageView
    private lateinit var btn_tesoura: ImageView
    private lateinit var tvAdversarios: TextView

    private lateinit var geradorRandom : Random
    private lateinit var jogadaUsuario: Jogada
    private lateinit var jogador1: Jogada
    private var jogador2: Jogada? = null
    private lateinit var configuracao: Configuracao
    private lateinit var configActivityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var dbConfiguracao: ConfigSQLite

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        dbConfiguracao = ConfigSQLite(this)

        val configAtual = dbConfiguracao.getConfig()

        //se a configuracao nao for encontrada, é aplicada a padrao e salva no db
        configuracao = configAtual?: Configuracao()
        if(configAtual == null){
            dbConfiguracao.adicionarConfig(configuracao)
        }

        supportActionBar?.title = "Jokenpo"
        geradorRandom = Random(System.currentTimeMillis())

        btn_pedra = findViewById(R.id.PedraBtn)
        btn_papel = findViewById(R.id.PapelBtn)
        btn_tesoura = findViewById(R.id.TesouraBtn)
        tvAdversarios = findViewById(R.id.qtddAdversarios)

        tvAdversarios.text = "Quantidade de adversários: "+ configuracao.numeroAdversarios

        activityMainBinding.jogarBtn.setOnClickListener {
            geraJogadasAdversarias()
            activityMainBinding.resultadoTv.text = verificaResultado().uppercase()
            activityMainBinding.jogadaUsuario.text = "Você escolheu ${jogadaUsuario.toString()}"
            activityMainBinding.jogadaJogador1.text = "Jogador 1 jogou ${jogador1.toString()}"
            if(jogador2 != null){
                activityMainBinding.jogadaJogador2.visibility = View.VISIBLE
                activityMainBinding.jogadaJogador2.text = "Jogador 2 jogou ${jogador2.toString()}"
            }
        }

        configActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == RESULT_OK){
                val novaConfig = result.data?.getParcelableExtra<Configuracao>(Intent.EXTRA_USER)
                if(novaConfig?.numeroAdversarios!! == 2 || novaConfig.numeroAdversarios == 1){
                    configuracao = novaConfig
                    dbConfiguracao.alterarConfig(configuracao)
                    resetaTextosJogadas()
                    destacarBotao(arrayOf(btn_papel, btn_pedra, btn_tesoura))
                    Toast.makeText(this, "Configuração atualizada!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun resetaTextosJogadas() {
        activityMainBinding.jogadaUsuario.text = ""
        activityMainBinding.jogadaJogador1.text = ""
        activityMainBinding.jogadaJogador2.visibility = View.GONE
        activityMainBinding.jogarBtn.isEnabled = false
        activityMainBinding.resultadoTv.text = ""
        tvAdversarios.text = "Quantidade de adversários: "+ configuracao.numeroAdversarios
    }

    private fun verificaResultado(): String {
        if(configuracao.numeroAdversarios == 1){
            return comparaJogadores(jogador1)
        }else{
            return comparaJogadores(jogador1, jogador2)
        }
    }

    private fun comparaJogadores(adversario: Jogada, adversario2: Jogada? = null) : String {
        if(adversario2 == null){
            when(jogadaUsuario){
                Jogada.PEDRA -> {
                    return when(adversario){
                        Jogada.PEDRA -> "Empate"
                        Jogada.PAPEL -> "Derrota"
                        Jogada.TESOURA -> "Vitoria"
                    }
                }
                Jogada.PAPEL -> {
                    return when(adversario){
                        Jogada.PEDRA -> "Vitoria"
                        Jogada.PAPEL -> "Empate"
                        Jogada.TESOURA -> "Derrota"
                    }
                }
                Jogada.TESOURA -> {
                    return when(adversario){
                        Jogada.PEDRA -> "Derrota"
                        Jogada.PAPEL -> "Vitoria"
                        Jogada.TESOURA -> "Empate"
                    }
                }
            }
        }else{
            when(jogadaUsuario){
                Jogada.PEDRA -> {
                    when(jogador1){
                        Jogada.PEDRA -> {
                            return when(jogador2){
                                Jogada.PAPEL -> {
                                    "Derrota"
                                }
                                else -> "Empate"
                            }
                        }
                        Jogada.PAPEL -> {
                            return when(jogador2){
                                Jogada.PEDRA -> {
                                    "Derrota"
                                }
                                else -> "Empate"
                            }
                        }
                        Jogada.TESOURA -> {
                            return when(jogador2){
                                Jogada.TESOURA -> {
                                    "Vitoria"
                                }
                                else -> "Empate"
                            }
                        }
                    }
                }
                Jogada.PAPEL -> {
                    when(jogador1){
                        Jogada.PEDRA -> {
                            return when(jogador2){
                                Jogada.PEDRA -> {
                                    "Vitoria"
                                }
                                else -> "Empate"
                            }
                        }
                        Jogada.PAPEL -> {
                            return when(jogador2){
                                Jogada.TESOURA -> {
                                    "Derrota"
                                }
                                else -> "Empate"
                            }
                        }
                        Jogada.TESOURA -> {
                            return when(jogador2){
                                Jogada.PAPEL -> {
                                    "Derrota"
                                }
                                else -> "Empate"
                            }
                        }
                    }
                }
                Jogada.TESOURA -> {
                    when(jogador1){
                        Jogada.PEDRA -> {
                            return when(jogador2){
                                Jogada.TESOURA -> {
                                    "Derrota"
                                }
                                else -> "Empate"
                            }
                        }
                        Jogada.PAPEL -> {
                            return when(jogador2){
                                Jogada.PAPEL -> {
                                    "Vitoria"
                                }
                                else -> "Empate"
                            }
                        }
                        Jogada.TESOURA -> {
                            return when(jogador2){
                                Jogada.PEDRA -> {
                                    "Derrota"
                                }
                                else -> "Empate"
                            }
                        }
                    }
                }
            }
        }
    }

    private fun geraJogadasAdversarias() {
        jogador1 = Jogada.values()[geradorRandom.nextInt(0..2)]
        jogador2 = if(configuracao.numeroAdversarios == 2){
            Jogada.values()[geradorRandom.nextInt(0..2)]
        }else{
            null
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId ==  R.id.configIt){
            val configIntent = Intent(this, ConfigActivity::class.java)
            configIntent.putExtra(Intent.EXTRA_USER, configuracao)
            configActivityResultLauncher.launch(configIntent)
        }
        return super.onOptionsItemSelected(item)
    }

    fun buttonClicked(view: View) {
        if (view.id == R.id.PedraBtn) {
            jogadaUsuario = Jogada.PEDRA
            activityMainBinding.jogarBtn.isEnabled = true
            destacarBotao(arrayOf(btn_tesoura, btn_papel), btn_pedra)
        } else if (view.id == R.id.PapelBtn) {
            jogadaUsuario = Jogada.PAPEL
            activityMainBinding.jogarBtn.isEnabled = true
            destacarBotao(arrayOf(btn_tesoura, btn_pedra), btn_papel)
        } else if (view.id == R.id.TesouraBtn) {
            jogadaUsuario = Jogada.TESOURA
            activityMainBinding.jogarBtn.isEnabled = true
            destacarBotao(arrayOf(btn_pedra, btn_papel), btn_tesoura)
        }
    }

    private fun destacarBotao(arrayOfImageViews: Array<ImageView>, imgPrincipal: ImageView? = null) {
        imgPrincipal?.layoutParams?.height = converteDpToPx(90)
        imgPrincipal?.requestLayout()
        for (img in arrayOfImageViews){
            img.layoutParams.height = converteDpToPx(70)
            img.requestLayout()
        }
    }

    private fun converteDpToPx(dp: Int): Int {
        val r: Resources = resources
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            r.displayMetrics
        ).toInt()
    }

}