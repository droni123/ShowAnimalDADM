package mx.com.idel.showanimaldadm.view.activity

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Typeface
import android.graphics.drawable.AnimatedVectorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.View
import android.view.View.TEXT_ALIGNMENT_CENTER
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import mx.com.idel.showanimaldadm.Constantes
import mx.com.idel.showanimaldadm.Constantes.EMAIL_ADDRESS_PATTERN
import mx.com.idel.showanimaldadm.R
import mx.com.idel.showanimaldadm.databinding.ActivityLoginBinding
import mx.com.idel.showanimaldadm.viewmodel.StartViewModel

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val startViewModel: StartViewModel by viewModels()
    private lateinit var anima: AnimatedVectorDrawable

    private var email = ""
    private var contrasenia = ""
    private var mensajetmp = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val spannable = SpannableStringBuilder(getString(R.string.app_name))
        spannable.setSpan(StyleSpan(Typeface.BOLD), 0, 3, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        binding.appname.text = spannable

        binding.iniciarsesion.setOnClickListener {
            if(!validaCampos()) return@setOnClickListener
            mostrarOrOcultarData()
            startViewModel.loginUser(email,contrasenia)
        }
        binding.registrarusuario.setOnClickListener {
            if(!validaCampos()) return@setOnClickListener
            mostrarOrOcultarData()
            startViewModel.registerUser(email,contrasenia)
        }

        binding.restablecer.setOnClickListener {
            val spannable = SpannableStringBuilder(getString(R.string.pop_restablecer_2))
            spannable.setSpan(StyleSpan(Typeface.BOLD), 8, 16, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
            val resetMail = EditText(it.context)
            resetMail.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            resetMail.setHint(getString(R.string.user_email))
            resetMail.textAlignment = TEXT_ALIGNMENT_CENTER
            val dialogo = MaterialAlertDialogBuilder(this@Login,R.style.MaterialAlertDialog__Center)
                .setTitle(getString(R.string.pop_restablecer))
                .setMessage(spannable)
                .setView(resetMail)
                .setPositiveButton(getString(R.string.aceptar), null)
                .setNegativeButton(getString(R.string.cancelar),null)
                //.show()
            val alerta = dialogo.create()
            alerta.setOnShowListener {
                val okButton = alerta.getButton(AlertDialog.BUTTON_POSITIVE)
                okButton.setOnClickListener {
                    val mail = resetMail.text.toString()
                    if(mail.isEmpty()){
                        Toast.makeText(this@Login,getString(R.string.error_correo),Toast.LENGTH_SHORT).show()
                    }else if(!isValidString(mail)){
                        Toast.makeText(this@Login,getString(R.string.error_correov),Toast.LENGTH_SHORT).show()
                    }else{
                        startViewModel.restartUser(mail)
                        alerta.dismiss()
                    }
                }
            }
            alerta.show()
        }
        startViewModel.usuario.observe(this){ usuarioData->
            if(usuarioData.id != 0){
                mostrarOrOcultarData()
                //ACCEDIO
                val vistaMainActivity = Intent(this@Login, MainActivity::class.java).apply {
                    putExtra(Constantes.KEY_BUNDLE_USER_DATA, usuarioData)
                }
                startActivity(vistaMainActivity)
                finish()
                overridePendingTransition(0, R.drawable.fade_screen)
            }
        }
        startViewModel.mensajes.observe(this){textorecibidoo->
            val textorecibido = textorecibidoo.trim()
            if(textorecibido.isNotEmpty()){
                mensajetmp = textorecibido
                Toast.makeText(this@Login,textorecibido,Toast.LENGTH_SHORT).show()
            }
        }
        startViewModel.ocultar.observe(this){enviando ->
            if(enviando){
                mostrarOrOcultarData()
            }else{
                mostrarOrOcultarData(true)
            }
        }
        startViewModel.focus.observe(this){selector->
            if(selector==1){
                binding.email.error = mensajetmp
                binding.email.requestFocus()
            }
            if(selector==2){
                binding.pass.error = mensajetmp
                binding.pass.requestFocus()
            }
        }
    }
    //icono
    private fun validaCampos(): Boolean{
        email = binding.inputemail.text.toString().trim()
        contrasenia = binding.inputpass.text.toString().trim()
        if(email.isEmpty()){
            binding.inputemail.error = getString(R.string.error_correo)
            binding.inputemail.requestFocus()
            return false
        }
        if(!isValidString(email)){
            binding.inputemail.error = getString(R.string.error_correov)
            binding.inputemail.requestFocus()
            return false
        }
        if(contrasenia.isEmpty() || contrasenia.length < 6){
            binding.inputpass.error = getString(R.string.error_pass)
            binding.inputpass.requestFocus()
            return false
        }
        return true
    }
    private fun mostrarOrOcultarData(mostrar: Boolean = false){
        if (mostrar) {
            binding.email.visibility = View.VISIBLE
            binding.pass.visibility = View.VISIBLE
            binding.botones.visibility = View.VISIBLE
            binding.divisor.visibility = View.VISIBLE
            binding.restablecer.visibility = View.VISIBLE
            binding.pie.visibility = View.VISIBLE
            binding.icono.setImageResource(R.drawable.ico)
        }else {
            binding.email.visibility = View.INVISIBLE
            binding.pass.visibility = View.INVISIBLE
            binding.botones.visibility = View.INVISIBLE
            binding.divisor.visibility = View.INVISIBLE
            binding.restablecer.visibility = View.INVISIBLE
            binding.pie.visibility = View.INVISIBLE
            binding.icono.setImageResource(R.drawable.load)
            anima = binding.icono.drawable as AnimatedVectorDrawable
            anima.start()
        }
    }
    fun isValidString(str: String): Boolean{
        return EMAIL_ADDRESS_PATTERN.matcher(str).matches()
    }
}