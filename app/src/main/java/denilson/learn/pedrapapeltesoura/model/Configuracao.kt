package denilson.learn.pedrapapeltesoura.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Configuracao (
    var numeroAdversarios: Int = 1,
) : Parcelable