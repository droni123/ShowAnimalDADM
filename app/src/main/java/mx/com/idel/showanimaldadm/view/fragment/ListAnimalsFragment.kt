package mx.com.idel.showanimaldadm.view.fragment

import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import mx.com.idel.showanimaldadm.R
import mx.com.idel.showanimaldadm.databinding.FragmentListAnimalsBinding
import mx.com.idel.showanimaldadm.model.entities.AnimalModel
import mx.com.idel.showanimaldadm.view.adapter.AnimalAdapter
import mx.com.idel.showanimaldadm.viewmodel.ListaViewModel

/**
 * Creado por De la Cruz Hernández Idelfonso el 14/01/23
 */
class ListAnimalsFragment : Fragment() {

    private var _binding: FragmentListAnimalsBinding? = null
    private val binding get() = _binding!!

    private val listaViewModel : ListaViewModel by viewModels()
    private lateinit var animalAdapter: AnimalAdapter
    private lateinit var anima: AnimatedVectorDrawable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentListAnimalsBinding.inflate(inflater, container, false)
        val view = binding.root
        anima = binding.loaddatos.drawable as AnimatedVectorDrawable

        val dataEmptyInitial = ArrayList<AnimalModel>()
        animalAdapter = AnimalAdapter(dataEmptyInitial)

        binding.listaAnimales.layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL,false)
        binding.listaAnimales.adapter = animalAdapter

        binding.refresh.setOnRefreshListener {
            binding.refresh.isRefreshing = false
            listaViewModel.getAnimalList()
        }
        listaViewModel.loader.observe(viewLifecycleOwner) {
            binding.loaddatos.visibility = if (it == true) View.VISIBLE else View.INVISIBLE
            binding.sinDatos.visibility = if (it == false && animalAdapter.itemCount <= 0) View.VISIBLE else View.INVISIBLE
            binding.listaAnimales.visibility = if (it == false && animalAdapter.itemCount > 0) View.VISIBLE else View.INVISIBLE
            if (it == true) anima.start() else anima.stop()
        }
        listaViewModel.listaAnimales.observe(viewLifecycleOwner, Observer {
            animalAdapter.updateDataItems(it)
            animalAdapter.itemCount
        })
        animalAdapter.onItemClick = { animal ->
            val accion = ListAnimalsFragmentDirections.actionListAnimalsFragmentToDetallAnimalFragment(animal)
            findNavController().navigate(accion)
        }
        listaViewModel.getAnimalList()
        // Inflate the layout for this fragment
        return view
    }
    override fun onResume() {
        super.onResume()
        listaViewModel.getAnimalList()
    }
}