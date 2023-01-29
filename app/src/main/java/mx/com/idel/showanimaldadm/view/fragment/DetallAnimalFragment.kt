package mx.com.idel.showanimaldadm.view.fragment

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.annotations.SerializedName
import mx.com.idel.showanimaldadm.R
import mx.com.idel.showanimaldadm.databinding.FragmentDetallAnimalBinding
import mx.com.idel.showanimaldadm.model.entities.AnimalModel
import mx.com.idel.showanimaldadm.viewmodel.ListaViewModel


/**
 * Creado por De la Cruz Hernández Idelfonso el 14/01/23
 */
class DetallAnimalFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentDetallAnimalBinding? = null
    private val binding get() = _binding!!

    private lateinit var anima: AnimatedVectorDrawable
    private var animalRecivido : AnimalModel = AnimalModel()
    private val args : DetallAnimalFragmentArgs by navArgs()
    private val listaViewModel : ListaViewModel by viewModels()

    //Para Google Maps
    private lateinit var map: GoogleMap

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
            animalDetall?.let { animalFromApy ->
                if(animalFromApy.id != 0) {
                    binding.NombreAnimal.text = animalFromApy.nombre
                    Glide.with(binding.root)
                        .load(animalFromApy.imagen)
                        .placeholder(R.drawable.icono)
                        .centerCrop()
                        .circleCrop()
                        .into(binding.imagenAnimal)
                    binding.duenoAnimal.text = HtmlCompat.fromHtml(
                        "<b>${getText(R.string.dueno)}</b>: ${animalFromApy.dueno}",
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    )
                    binding.pesoAnimal.text = HtmlCompat.fromHtml(
                        "<b>${getText(R.string.peso)}</b>: ${animalFromApy.peso}kg",
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    )
                    binding.enfermoAnimal.isEnabled = false
                    binding.enfermoAnimal.isChecked = animalFromApy.enfermo == true
                    for (i in 0 until binding.generoAnimal.childCount) {
                        binding.generoAnimal.getChildAt(i).isEnabled = false
                    }
                    when (animalFromApy.genero == true) {
                        true -> binding.generoAnimal.check(R.id.HAnimal)
                        false -> binding.generoAnimal.check(R.id.MAnimal)
                    }
                    binding.descripcionAnimal.text = HtmlCompat.fromHtml(
                        "<b>${getText(R.string.descripcion)}</b>: ${animalFromApy.descripcion}",
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    )

                    setPosisionAnimal(animalFromApy.latitude,animalFromApy.longitude)
                }else{
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.error_dataAnimal,animalRecivido.nombre),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } ?: run {
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

        val googleMapsFragment : SupportMapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        googleMapsFragment.getMapAsync(this)

        return view
    }
    override fun onResume() {
        super.onResume()
        listaViewModel.getAnimalData(animalRecivido.id)
    }
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
    }
    //latitude:20.936387
    //longitude:-98.196089
    fun setPosisionAnimal(latitude: Double? = 20.936387, longitude: Double? =-98.196089){
        val coordinates = LatLng(latitude ?: 20.936387, longitude ?: -98.196089)
        val marker = MarkerOptions()
            .position(coordinates)
            //.title("Animal")
            //.snippet("Descripcion")
            .icon(bitmapDescriptorFromVector(requireContext(), R.drawable.ico_green))

        map.addMarker(marker)
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(coordinates, 18f),
            4000,
            null
        )
    }
    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable!!.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}