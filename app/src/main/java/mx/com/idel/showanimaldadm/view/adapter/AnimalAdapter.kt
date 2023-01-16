package mx.com.idel.showanimaldadm.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import mx.com.idel.showanimaldadm.R
import mx.com.idel.showanimaldadm.databinding.RecyclerviewItemAnimalBinding
import mx.com.idel.showanimaldadm.model.entities.AnimalModel

/**
 * Creado por De la Cruz Hernández Idelfonso el 14/01/23
 */
class AnimalAdapter(private var items: ArrayList<AnimalModel>):RecyclerView.Adapter<AnimalAdapter.AnimalHolderView>() {

    var onItemClick: ((AnimalModel)->Unit)? = null

    class AnimalHolderView(val binding:RecyclerviewItemAnimalBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(animal:AnimalModel,onItemClick:((AnimalModel) -> Unit)?){
            binding.animal = animal
            Glide.with(binding.root)
                .load(animal.imagen)
                .placeholder(R.drawable.load)
                .centerCrop()
                .into(binding.imagenAnimal)
            binding.cardview.setOnClickListener {
                onItemClick?.invoke(animal)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalHolderView {
        val brinding = RecyclerviewItemAnimalBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AnimalHolderView(brinding)
    }

    override fun onBindViewHolder(holder: AnimalHolderView, position: Int) {
        holder.bind(items[position],onItemClick)
    }

    override fun getItemCount(): Int = items.size

    fun updateDataItems(newList : ArrayList<AnimalModel>){
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }
}