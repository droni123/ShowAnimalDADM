package mx.com.idel.showanimaldadm.view.activity

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.widget.Toast
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import mx.com.idel.showanimaldadm.Constantes
import mx.com.idel.showanimaldadm.R
import mx.com.idel.showanimaldadm.databinding.ActivityMainBinding
import mx.com.idel.showanimaldadm.model.entities.UserPerfil
import mx.com.idel.showanimaldadm.view.fragment.DetallAnimalFragmentDirections
import mx.com.idel.showanimaldadm.view.fragment.ListAnimalsFragmentDirections
import mx.com.idel.showanimaldadm.viewmodel.StartViewModel
/**
 * Creado por De la Cruz Hernández Idelfonso el 14/01/23
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController : NavController
    private val startViewModel: StartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.extras?.let { bundle ->
            if (bundle.containsKey(Constantes.KEY_BUNDLE_USER_DATA)) {
                val usuarioData = bundle.getSerializable(Constantes.KEY_BUNDLE_USER_DATA) as UserPerfil
                printDataUser(usuarioData)
            }else{ inicializarConfig() }
        } ?: inicializarConfig()

        binding.acercade.setOnClickListener {
            val spannable = SpannableStringBuilder(getString(R.string.creditos_datos))
            spannable.setSpan(StyleSpan(Typeface.BOLD), 15, 45, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
            MaterialAlertDialogBuilder(this@MainActivity,R.style.MaterialAlertDialog__Center)
                .setTitle(getString(R.string.creditos))
                .setMessage(spannable)
                .setPositiveButton(getString(R.string.aceptar),null)
                .show()
        }
        binding.avatar.setOnClickListener {
            val nC = findNavController(R.id.MainRoot)
            val fragmentActual = nC.currentDestination?.label
            when (fragmentActual){
                Constantes.NAME_FRAGMENT_LIST ->{
                    val accion = ListAnimalsFragmentDirections.actionListAnimalsFragmentToPerfilConfigFragment()
                    nC.navigate(accion)
                }
                Constantes.NAME_FRAGMENT_DETAIL -> {
                    val accion =
                        DetallAnimalFragmentDirections.actionDetallAnimalFragmentToPerfilConfigFragment()
                    nC.navigate(accion)
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController,null)
    }
    override fun onResume() {
        super.onResume()
        inicializarConfig()
    }
    private fun inicializarConfig(){
        startViewModel.usuario.observe(this){ usuarioData->
            if(usuarioData.id != 0){
                printDataUser(usuarioData)
            }else{
                Toast.makeText(
                    this,
                    getString(R.string.error_dataUser,usuarioData.id.toString()),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        startViewModel.getDataUser(Constantes.KEY_ID_USER,)
    }
    fun printDataUser(usuarioData:UserPerfil = UserPerfil()){
        val spannable = SpannableStringBuilder("${getString(R.string.bienvenido)}:\n${usuarioData.nombre}")
        spannable.setSpan(StyleSpan(Typeface.BOLD), 13, 16, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        spannable.setSpan(StyleSpan(Typeface.BOLD), 22, spannable.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        binding.nombreusuario.text = spannable
        Glide.with(this@MainActivity)
            .load(usuarioData.imagen)
            .placeholder(R.drawable.load)
            .centerCrop()
            .circleCrop()
            .into(binding.avatar)
    }
}