package mx.com.idel.showanimaldadm.model.entities

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Creado por De la Cruz Hernández Idelfonso el 15/01/23
 */
data class AnimalModelDetall(
    @SerializedName("id")
    var id:Int? = 0,
    @SerializedName("nombre")
    var nombre:String? = "Nombre de animal",
    @SerializedName("imagen")
    var imagen:String? = "https://idel.com.mx/dadm/oso.gif",
    @SerializedName("dueno")
    var dueno:String? = "Pancho pantera",
    @SerializedName("peso")
    val peso: Double? = 5.00,
    @SerializedName("genero")
    var genero:Boolean? = false,
    @SerializedName("enfermo")
    var enfermo:Boolean? = false,
    @SerializedName("descripcion")
    var descripcion:String? = "Esta es una descripción de ejemplo",
    @SerializedName("latitude")
    var latitude:Double? = 49.935997,
    @SerializedName("longitude")
    var longitude:Double? = 40.553506
) : Serializable