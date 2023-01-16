package mx.com.idel.showanimaldadm.viewmodel

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mx.com.idel.showanimaldadm.Constantes
import mx.com.idel.showanimaldadm.model.entities.AnimalModelDetall
import mx.com.idel.showanimaldadm.model.entities.UserPerfil
import mx.com.idel.showanimaldadm.model.repository.DataApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Creado por De la Cruz Hernández Idelfonso el 15/01/23
 */
class StartViewModel:ViewModel() {
    val usuario = MutableLiveData<UserPerfil>()
    fun getDataUser(id: Int){
        Handler(Looper.getMainLooper()).postDelayed({
            CoroutineScope(Dispatchers.IO).launch {
                val call = Constantes
                    .retrofint()
                    .create(DataApi::class.java)
                    .getUserData(id)
                call.enqueue(
                    object : Callback<UserPerfil>{
                        override fun onResponse(
                            call: Call<UserPerfil>,
                            response: Response<UserPerfil>
                        ) {
                            try {
                                if(response.code() == 200){
                                    usuario.postValue( response.body() )
                                }else{
                                    Log.e(Constantes.LOGTAG,"Error en datos: ${response.code()}")
                                    usuario.postValue( UserPerfil() )
                                }
                            }catch (ex:Exception){
                                Log.e(Constantes.LOGTAG,"Error en datos: ${ex.toString()}")
                                usuario.postValue( UserPerfil() )
                            }
                        }
                        override fun onFailure(call: Call<UserPerfil>, t: Throwable) {
                            Log.d(Constantes.LOGTAG, "Error servidor: ${t.message}")
                            usuario.postValue( UserPerfil() )
                        }
                    }
                )
            }
        },Constantes.KEY_TIEMPO_DE_ESPERA_STAR)
    }
}