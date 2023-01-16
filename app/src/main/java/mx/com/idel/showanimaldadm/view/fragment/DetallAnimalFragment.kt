package mx.com.idel.showanimaldadm.view.fragment

import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import mx.com.idel.showanimaldadm.R
import mx.com.idel.showanimaldadm.databinding.FragmentDetallAnimalBinding
import mx.com.idel.showanimaldadm.model.entities.AnimalModel
import mx.com.idel.showanimaldadm.model.entities.AnimalModelDetall
import mx.com.idel.showanimaldadm.viewmodel.ListaViewModel

/**
 * Creado por De la Cruz Hernández Idelfonso el 14/01/23
 */
class DetallAnimalFragment : Fragment() {

    private var _binding: FragmentDetallAnimalBinding? = null
    private val binding get() = _binding!!

    private lateinit var anima: AnimatedVectorDrawable
    private var animalRecivido : AnimalModel = AnimalModel()
    private val args : DetallAnimalFragmentArgs by navArgs()
    private val listaViewModel : ListaViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetallAnimalBinding.inflate(inflater, container, false)
        val view = binding.root

        animalRecivido = args.animal
        binding.NombreAnimal.text = animalRecivido?.nombre
        Glide.with(binding.root)
            .load(animalRecivido?.imagen)
            .placeholder(R.drawable.icono)
            .centerCrop()
            .circleCrop()
            .into(binding.imagenAnimal)

        anima = binding.loaddatos.drawable as AnimatedVectorDrawable
        listaViewModel.loader.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.loaddatos.visibility = View.VISIBLE
                anima.start()
                binding.duenoAnimal.visibility = View.INVISIBLE
                binding.pesoAnimal.visibility = View.INVISIBLE
                binding.generoAnimal.visibility = View.INVISIBLE
                binding.enfermoAnimal.visibility = View.INVISIBLE
                binding.descripcionAnimal.visibility = View.INVISIBLE
            }else {
                binding.loaddatos.visibility = View.INVISIBLE
                anima.stop()
                binding.duenoAnimal.visibility = View.VISIBLE
                binding.pesoAnimal.visibility = View.VISIBLE
                binding.generoAnimal.visibility = View.VISIBLE
                binding.enfermoAnimal.visibility = View.VISIBLE
                binding.descripcionAnimal.visibility = View.VISIBLE
                listaViewModel.animal.observe(viewLifecycleOwner, Observer { animalDetall ->
                    if(animalDetall.id == 0){
                        binding.generoAnimal.visibility = View.INVISIBLE
                        binding.enfermoAnimal.visibility = View.INVISIBLE
                    }
                })
            }
        }
        listaViewModel.animal.observe(viewLifecycleOwner, Observer { animalDetall ->
            if(animalDetall.id != 0) {
                binding.NombreAnimal.text = animalDetall?.nombre
                Glide.with(binding.root)
                    .load(animalDetall?.imagen)
                    .placeholder(R.drawable.icono)
                    .centerCrop()
                    .circleCrop()
                    .into(binding.imagenAnimal)
                binding.duenoAnimal.text = HtmlCompat.fromHtml(
                    "<b>${getText(R.string.dueno)}</b>: ${animalDetall?.dueno}",
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
                binding.pesoAnimal.text = HtmlCompat.fromHtml(
                    "<b>${getText(R.string.peso)}</b>: ${animalDetall?.peso}kg",
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
                binding.enfermoAnimal.isEnabled = false
                binding.enfermoAnimal.isChecked = animalDetall?.enfermo == true
                for (i in 0 until binding.generoAnimal.childCount) {
                    binding.generoAnimal.getChildAt(i).isEnabled = false
                }
                when (animalDetall?.genero == true) {
                    true -> binding.generoAnimal.check(R.id.HAnimal)
                    false -> binding.generoAnimal.check(R.id.MAnimal)
                }
                binding.descripcionAnimal.text = HtmlCompat.fromHtml(
                    "<b>${getText(R.string.descripcion)}</b>: ${animalDetall?.descripcion}",
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            }else{
                Toast.makeText(
                    requireContext(),
                    getString(R.string.error_dataAnimal,animalRecivido.nombre),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }
        listaViewModel.getAnimalData(animalRecivido.id)
        return view
    }
    override fun onResume() {
        super.onResume()
        listaViewModel.getAnimalData(animalRecivido.id)
    }
}