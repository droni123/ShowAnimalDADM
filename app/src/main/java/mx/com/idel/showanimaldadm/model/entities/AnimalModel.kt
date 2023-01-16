package mx.com.idel.showanimaldadm.model.entities

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Creado por De la Cruz Hernández Idelfonso el 14/01/23
 */
data class AnimalModel(
    @SerializedName("id")
    var id:Int? = 0,
    @SerializedName("nombre")
    var nombre:String? = "Nombre de animal",
    @SerializedName("imagen")
    var imagen:String? = "https://idel.com.mx/dadm/oso.gif",
) : Serializable
