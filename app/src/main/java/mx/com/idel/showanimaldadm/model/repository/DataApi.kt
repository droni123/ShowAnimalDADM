package mx.com.idel.showanimaldadm.model.repository

import mx.com.idel.showanimaldadm.model.entities.AnimalModel
import mx.com.idel.showanimaldadm.model.entities.AnimalModelDetall
import mx.com.idel.showanimaldadm.model.entities.UserPerfil
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Creado por De la Cruz Hernández Idelfonso el 15/01/23
 */
interface DataApi {
    @GET("user")
    fun getUserData(
        @Query("id") id: Int?
    ): Call<UserPerfil>

    @GET("animalesall")
    fun getAnimalAll(): Call<ArrayList<AnimalModel>>

    @GET("animal")
    fun getAnimal(
        @Query("id") id: Int?
    ): Call<AnimalModelDetall>
}