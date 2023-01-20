package mx.com.idel.showanimaldadm.model.repository

import mx.com.idel.showanimaldadm.Constantes
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Creado por De la Cruz Hernández Idelfonso el 20/01/23
 */
object RetrofitService {
    private var INSTANCE: Retrofit? = null
    fun getRetrofit(): Retrofit {
        return INSTANCE ?: synchronized(this){
            val instance = Retrofit.Builder()
                .baseUrl(Constantes.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            INSTANCE = instance
            instance
        }
    }
}