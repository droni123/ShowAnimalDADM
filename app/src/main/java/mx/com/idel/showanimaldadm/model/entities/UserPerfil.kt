package mx.com.idel.showanimaldadm.model.entities

import com.google.gson.annotations.SerializedName
import java.io.Serializable
/**
 * Creado por De la Cruz Hernández Idelfonso el 15/01/23
 */
data class UserPerfil(
    @SerializedName("id")
    var id:Int? = 0,
    @SerializedName("nombre")
    var nombre:String? = "Nombre de usuario",
    @SerializedName("imagen")
    var imagen:String? = "https://idel.com.mx/imagenes/info/foto.jpg",
): Serializable
