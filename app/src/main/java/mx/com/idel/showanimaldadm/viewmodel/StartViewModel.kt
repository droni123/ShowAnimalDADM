package mx.com.idel.showanimaldadm.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mx.com.idel.showanimaldadm.App
import mx.com.idel.showanimaldadm.Constantes
import mx.com.idel.showanimaldadm.R
import mx.com.idel.showanimaldadm.model.entities.UserPerfil
import mx.com.idel.showanimaldadm.model.repository.DataApi
import mx.com.idel.showanimaldadm.model.repository.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Creado por De la Cruz Hernández Idelfonso el 15/01/23
 */

class StartViewModel:ViewModel() {
    val usuario = MutableLiveData<UserPerfil>()
    private var usuario_interno:UserPerfil = UserPerfil()
    val ocultar = MutableLiveData<Boolean>()
    val mensajes = MutableLiveData<String>()
    val focus = MutableLiveData<Int>()
    val verificacion  = MutableLiveData<Boolean>()
    //Para Firebase
    private var firebaseAuth:FirebaseAuth? = null
    private var userAuth: FirebaseUser? = null
    //val bandera
    private var con_internet = true
    private var esperando_actualizar_usuario = false
    init {
        firebaseAuth = FirebaseAuth.getInstance()

        val connectivityManager = App.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                it.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        if(!con_internet){
                            Toast.makeText(App.instance,"REGRESO INTERNET", Toast.LENGTH_SHORT).show()
                            con_internet = true
                            if(esperando_actualizar_usuario){
                                getDataUser()
                                esperando_actualizar_usuario = false
                            }
                        }
                    }
                    override fun onLost(network: Network) {
                        con_internet = false
                        Toast.makeText(App.instance,"SIN CONEXION", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }
    fun getDataUser(start:Boolean = false){
        var tiempo = Constantes.KEY_TIEMPO_DE_ESPERA
        if(start){ tiempo = Constantes.KEY_TIEMPO_DE_ESPERA_STAR  }
        Handler(Looper.getMainLooper()).postDelayed({
            userAuth = firebaseAuth?.currentUser
            userAuth?.let {
                SendDataUserToView(it)
            } ?: kotlin.run {
                usuarioPostValue( UserPerfil() )
            }
        },tiempo)
    }
    fun loginUser(email:String,contrasenia:String){
        ocultar.postValue(true)
        Handler(Looper.getMainLooper()).postDelayed({
            firebaseAuth?.let {
                it.signInWithEmailAndPassword(email,contrasenia).addOnCompleteListener {authResult->
                    if(authResult.isSuccessful){
                        val user_fb = it.currentUser
                        mensajes.postValue(App.instance.getString(R.string.EXITO))
                        SendDataUserToView(user_fb)
                    }else{
                        manejaErrores(authResult)
                        ocultar.postValue(false)
                    }

                }
            }
        },Constantes.KEY_TIEMPO_DE_ESPERA)
    }
    fun registerUser(email:String,contrasenia:String){
        ocultar.postValue(true)
        Handler(Looper.getMainLooper()).postDelayed({
            firebaseAuth?.let {
                it.createUserWithEmailAndPassword(email, contrasenia)
                    .addOnCompleteListener { authResult ->
                        if (authResult.isSuccessful) {
                            //envia correo de verificacion
                            val user_fb = it.currentUser
                            user_fb?.sendEmailVerification()?.addOnCompleteListener {
                                mensajes.postValue(App.instance.getString(R.string.verificacion_ok))
                            }?.addOnFailureListener {
                                mensajes.postValue(App.instance.getString(R.string.verificacion_danger))
                            }
                            mensajes.postValue(App.instance.getString(R.string.user_create))
                            SendDataUserToView(user_fb)
                        } else {
                            manejaErrores(authResult)
                            ocultar.postValue(false)
                        }
                    }
            }
        },Constantes.KEY_TIEMPO_DE_ESPERA)
    }
    fun restartUser(email:String){
        ocultar.postValue(true)
        Handler(Looper.getMainLooper()).postDelayed({
            firebaseAuth?.let {
                it.sendPasswordResetEmail(email).addOnSuccessListener{
                    mensajes.postValue(App.instance.getString(R.string.pop_restablecer_exito))
                }.addOnFailureListener{ex->
                    mensajes.postValue(App.instance.getString(R.string.ERROR_ENLACE_RESET,ex.message))
                }
            }
            ocultar.postValue(false)
        },Constantes.KEY_TIEMPO_DE_ESPERA)
    }
    fun checkVerificacion(){
        //revisamos si el email no está verificado
        userAuth = firebaseAuth?.currentUser
        if(userAuth?.isEmailVerified != true) {
            verificacion.postValue(false)
        } else {
            verificacion.postValue(true)
        }
    }
    fun sendverificaion(){
        userAuth = firebaseAuth?.currentUser
        userAuth.let {
            it?.sendEmailVerification()?.addOnSuccessListener{
                mensajes.postValue(App.instance.getString(R.string.send_ver))
                cerrarsession()
            }?.addOnFailureListener{
                mensajes.postValue(App.instance.getString(R.string.ERROR_,App.instance.getString(R.string.send_ver_error)))
                cerrarsession()
            }
        }
    }
    fun cerrarsession(){
        firebaseAuth?.currentUser?.let {
            firebaseAuth?.signOut()
            usuarioPostValue( UserPerfil() )
        }
    }
    private fun manejaErrores(task: Task<AuthResult>){
        var errorCode = ""
        try{
            errorCode = (task.exception as FirebaseAuthException).errorCode
        }catch(e: Exception){
            errorCode = "NO_NETWORK"
        }
        when(errorCode){
            "ERROR_INVALID_EMAIL" -> {
                mensajes.postValue(  App.instance.getString(R.string.ERROR_,App.instance.getString(R.string.ERROR_INVALID_EMAIL)) )
                focus.postValue(1)
            }
            "ERROR_WRONG_PASSWORD" -> {
                mensajes.postValue(  App.instance.getString(R.string.ERROR_,App.instance.getString(R.string.ERROR_WRONG_PASSWORD)) )
                focus.postValue(2)
            }
            "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL" -> {
                mensajes.postValue(  App.instance.getString(R.string.ERROR_,App.instance.getString(R.string.ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL)) )
            }
            "ERROR_EMAIL_ALREADY_IN_USE" -> {
                mensajes.postValue(  App.instance.getString(R.string.ERROR_,App.instance.getString(R.string.ERROR_EMAIL_ALREADY_IN_USE)) )
                focus.postValue(1)
            }
            "ERROR_USER_TOKEN_EXPIRED" -> {
                mensajes.postValue(  App.instance.getString(R.string.ERROR_,App.instance.getString(R.string.ERROR_USER_TOKEN_EXPIRED)) )
            }
            "ERROR_USER_NOT_FOUND" -> {
                mensajes.postValue(  App.instance.getString(R.string.ERROR_,App.instance.getString(R.string.ERROR_USER_NOT_FOUND)) )
            }
            "ERROR_WEAK_PASSWORD" -> {
                mensajes.postValue(  App.instance.getString(R.string.ERROR_,App.instance.getString(R.string.ERROR_WEAK_PASSWORD)) )
                focus.postValue(2)
            }
            "NO_NETWORK" -> {
                mensajes.postValue(  App.instance.getString(R.string.ERROR_,App.instance.getString(R.string.NO_NETWORK)) )
            }
            else -> {
                mensajes.postValue(  App.instance.getString(R.string.ERROR_,App.instance.getString(R.string.ERROR_AUTH_OTRO)) )
            }
        }
    }
    @SuppressLint("SuspiciousIndentation")
    private fun SendDataUserToView(user: FirebaseUser?){
        var id_user_random = (1..5).random()
        if(usuario_interno.id != 0){ id_user_random = usuario_interno.id ?: id_user_random }
        viewModelScope.launch {
            val call = RetrofitService
                .getRetrofit()
                .create(DataApi::class.java)
                .getUserData(id_user_random)
                call.enqueue(
                    object : Callback<UserPerfil>{
                        override fun onResponse(
                            call: Call<UserPerfil>,
                            response: Response<UserPerfil>
                        ) {
                            var usertosend = UserPerfil(1,email = user?.email)
                            try {
                                if(response.code() == 200){
                                    usertosend = response.body() ?: UserPerfil()
                                    usertosend.email = user?.email
                                    usuarioPostValue( usertosend )
                                }else{
                                    mensajes.postValue( App.instance.getString(R.string.ERROR_DATOS_USER_API,response.code().toString()) )
                                    usuarioPostValue( usertosend )
                                }
                            }catch (ex:Exception){
                                Log.e(Constantes.LOGTAG,App.instance.getString(R.string.ERROR_DATOS_USER_API, ex.toString()))
                                usuarioPostValue( usertosend )
                            }
                            ocultar.postValue(false)
                        }
                        override fun onFailure(call: Call<UserPerfil>, t: Throwable) {
                            Log.d(Constantes.LOGTAG, App.instance.getString(R.string.ERROR_SERVER_API,t.message))
                            val usertosend = UserPerfil(id=1, email = user?.email)
                            usuarioPostValue( usertosend )
                            ocultar.postValue(false)
                            esperando_actualizar_usuario = true
                            con_internet = false
                        }
                    }
                )
        }
    }
    fun usuarioPostValue(usertosend:UserPerfil){
        usuario.postValue( usertosend )
        usuario_interno = usertosend
        if( App.instance.getString(R.string.cuser_nombre) == usertosend.nombre ){
            esperando_actualizar_usuario = true
            con_internet = false
        }
    }
}