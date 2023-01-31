package mx.com.idel.showanimaldadm.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mx.com.idel.showanimaldadm.App
import mx.com.idel.showanimaldadm.Constantes
import mx.com.idel.showanimaldadm.model.entities.AnimalModel
import mx.com.idel.showanimaldadm.model.entities.AnimalModelDetall
import mx.com.idel.showanimaldadm.model.entities.UserPerfil
import mx.com.idel.showanimaldadm.model.repository.DataApi
import mx.com.idel.showanimaldadm.model.repository.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Creado por De la Cruz Hernández Idelfonso el 15/01/23
 */
class ListaViewModel : ViewModel()  {
    val listaAnimales = MutableLiveData<ArrayList<AnimalModel>>()
    val loader = MutableLiveData<Boolean>()
    val animal = MutableLiveData<AnimalModelDetall>()
    /////
    private var con_internet = true
    private var esperando_actualizar_animales = false
    private var esperando_actualizar_animal = 0
    init {
        val connectivityManager = App.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                it.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        if(!con_internet){
                            con_internet = true
                            if(esperando_actualizar_animales){
                                getAnimalList()
                                esperando_actualizar_animales = false
                            }
                            if(esperando_actualizar_animal != 0){
                                getAnimalData(esperando_actualizar_animal)
                                esperando_actualizar_animal = 0
                            }
                        }
                    }
                    override fun onLost(network: Network) {
                        con_internet = false
                    }
                })
            }
        }
    }
    fun getAnimalList(){
        loader.postValue(true)
        Handler(Looper.getMainLooper()).postDelayed({
            CoroutineScope(Dispatchers.IO).launch {
                val call = RetrofitService
                    .getRetrofit()
                    .create(DataApi::class.java)
                    .getAnimalAll()
                call.enqueue(
                    object : Callback<ArrayList<AnimalModel>> {
                        override fun onResponse(
                            call: Call<ArrayList<AnimalModel>>,
                            response: Response<ArrayList<AnimalModel>>
                        ) {
                            try {
                                if(response.code() == 200) {
                                    listaAnimales.postValue(response.body())
                                }else{
                                    listaAnimales.postValue( ArrayList<AnimalModel>() )
                                }
                                loader.postValue(false)
                            }catch (ex:Exception){
                                Log.e(Constantes.LOGTAG,"Error en datos: ${ex.toString()}")
                                listaAnimales.postValue( ArrayList<AnimalModel>() )
                                loader.postValue(false)
                            }
                        }
                        override fun onFailure(call: Call<ArrayList<AnimalModel>>, t: Throwable) {
                            Log.d(Constantes.LOGTAG, "Error servidor: ${t.message}")
                            listaAnimales.postValue( ArrayList<AnimalModel>() )
                            loader.postValue(false)
                            con_internet = false
                            esperando_actualizar_animales = true
                        }
                    }
                )
            }
        }, Constantes.KEY_TIEMPO_DE_ESPERA)
    }
    fun getAnimalData(id:Int? = 0){
        loader.postValue(true)
        Handler(Looper.getMainLooper()).postDelayed({
            CoroutineScope(Dispatchers.IO).launch {
                val call = RetrofitService
                    .getRetrofit()
                    .create(DataApi::class.java)
                    .getAnimal(id)
                call.enqueue(
                    object : Callback<AnimalModelDetall> {
                        override fun onResponse(
                            call: Call<AnimalModelDetall>,
                            response: Response<AnimalModelDetall>
                        ) {
                            try {
                                if(response.code() == 200) {
                                    animal.postValue(response.body())
                                }else{
                                    animal.postValue( AnimalModelDetall() )
                                }
                                loader.postValue(false)
                            }catch (ex:Exception){
                                Log.e(Constantes.LOGTAG,"Error en datos: ${ex.toString()}")
                                animal.postValue( AnimalModelDetall() )
                                loader.postValue(false)
                            }
                        }
                        override fun onFailure(call: Call<AnimalModelDetall>, t: Throwable) {
                            Log.d(Constantes.LOGTAG, "Error servidor: ${t.message}")
                            animal.postValue( AnimalModelDetall() )
                            loader.postValue(false)
                            con_internet = false
                            esperando_actualizar_animal = id ?: 0
                        }
                    }
                )
            }
        }, Constantes.KEY_TIEMPO_DE_ESPERA)
    }
}