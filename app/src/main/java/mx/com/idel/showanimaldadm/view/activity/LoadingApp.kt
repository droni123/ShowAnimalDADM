package mx.com.idel.showanimaldadm.view.activity

import android.content.Intent
import android.graphics.Typeface
import android.graphics.drawable.AnimatedVectorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.widget.Toast
import androidx.activity.viewModels
import mx.com.idel.showanimaldadm.Constantes
import mx.com.idel.showanimaldadm.R
import mx.com.idel.showanimaldadm.databinding.ActivityLoadingAppBinding
import mx.com.idel.showanimaldadm.viewmodel.StartViewModel
/**
 * Creado por De la Cruz Hernández Idelfonso el 14/01/23
 */
class LoadingApp : AppCompatActivity() {
    private lateinit var binding:ActivityLoadingAppBinding
    private val startViewModel:StartViewModel by viewModels()
    private lateinit var anima: AnimatedVectorDrawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadingAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        anima = binding.loadImg.drawable as AnimatedVectorDrawable
        anima.start()

        val spannable = SpannableStringBuilder(getString(R.string.app_name))
        spannable.setSpan(StyleSpan(Typeface.BOLD), 0, 3, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        binding.appname.text = spannable

        inicializarConfig()
    }

    override fun onResume() {
        super.onResume()
        anima.start()
    }

    override fun onPause() {
        super.onPause()
        anima.stop()
    }
    private fun inicializarConfig(){
        startViewModel.usuario.observe(this){ usuarioData->
            if(usuarioData.id != 0){
                val vistaMainActivity = Intent(this@LoadingApp, MainActivity::class.java).apply {
                    putExtra(Constantes.KEY_BUNDLE_USER_DATA, usuarioData)
                }
                startActivity(vistaMainActivity)
                finish()
                overridePendingTransition(0, R.drawable.fade_screen)
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
}