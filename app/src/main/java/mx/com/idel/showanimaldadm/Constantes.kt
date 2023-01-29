package mx.com.idel.showanimaldadm

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.regex.Pattern

/**
 * Creado por De la Cruz Hernández Idelfonso el 14/01/23
 */
object Constantes {
    const val KEY_TIEMPO_DE_ESPERA_STAR:Long = 3000
    const val KEY_BUNDLE_USER_DATA = "INFO_USER_DATA"
    const val KEY_TIEMPO_DE_ESPERA:Long = 0
    const val NAME_FRAGMENT_LIST = "fragment_list_animals"
    const val NAME_FRAGMENT_DETAIL = "fragment_detall_animal"
    const val KEY_ID_USER = 1
    const val BASE_URL = "https://private-19eea8-idel.apiary-mock.com/"
    const val LOGTAG = "LOGSDRONI"
    val EMAIL_ADDRESS_PATTERN = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )
}