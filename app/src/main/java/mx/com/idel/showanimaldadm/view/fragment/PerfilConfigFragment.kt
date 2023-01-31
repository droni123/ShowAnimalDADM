package mx.com.idel.showanimaldadm.view.fragment

import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import mx.com.idel.showanimaldadm.R
import mx.com.idel.showanimaldadm.databinding.FragmentPerfilConfigBinding
import mx.com.idel.showanimaldadm.view.activity.MainActivity
import mx.com.idel.showanimaldadm.viewmodel.StartViewModel

/**
 * Creado por De la Cruz Hernández Idelfonso el 14/01/23
 */
class PerfilConfigFragment : Fragment() {
    private var _binding: FragmentPerfilConfigBinding? = null
    private val binding get() = _binding!!
    private val startViewModel: StartViewModel by viewModels()
    private lateinit var anima: AnimatedVectorDrawable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPerfilConfigBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.icono.setImageResource(R.drawable.load)
        anima = binding.icono.drawable as AnimatedVectorDrawable
        // Inflate the layout for this fragment
        inicializarConfig()
        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.cerrarsession.setOnClickListener {
            startViewModel.cerrarsession()
        }
        return view
    }
    private fun inicializarConfig(){
        HiddeoOrShowItems(false)
        startViewModel.usuario.observe(viewLifecycleOwner, Observer { usuarioData->
            if(usuarioData.id != 0){
                binding.inputnombre.setText(usuarioData.nombre)
                Glide.with(requireContext())
                    .load(usuarioData.imagen)
                    .placeholder(R.drawable.load)
                    .centerCrop()
                    .circleCrop()
                    .into(binding.icono)
                binding.inputavatar.setText(usuarioData.imagen)
                binding.inputusermail.setText(usuarioData.email)
                HiddeoOrShowItems(true)
            }else{
                Toast.makeText(
                    requireContext(),
                    getText(R.string.se_cerrar_session),
                    Toast.LENGTH_SHORT
                ).show()
                binding.back.visibility = View.VISIBLE
                anima.stop()
            }
            (activity as MainActivity).printDataUser(usuarioData)
        })
        startViewModel.usuarioPostValue( (activity as MainActivity).returnDataUser() )
    }
    private fun HiddeoOrShowItems(show:Boolean = true){
        if(show) {
            binding.nombre.visibility = View.VISIBLE
            binding.avatar.visibility = View.VISIBLE
            binding.back.visibility = View.VISIBLE
            binding.usermail.visibility = View.VISIBLE
            binding.cerrarsession.visibility = View.VISIBLE
            anima.stop()
        }else{
            binding.nombre.visibility = View.INVISIBLE
            binding.avatar.visibility = View.INVISIBLE
            binding.back.visibility = View.INVISIBLE
            binding.usermail.visibility = View.INVISIBLE
            binding.cerrarsession.visibility = View.INVISIBLE
            anima.start()
        }
    }
}