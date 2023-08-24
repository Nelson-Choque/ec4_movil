package pe.idat.flickrlistadatos.adapter

import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import coil.load
import kotlinx.coroutines.launch
import pe.idat.flickrlistadatos.R
import pe.idat.flickrlistadatos.databinding.ItemPhotoBinding
import pe.idat.flickrlistadatos.model.Photo
import pe.idat.flickrlistadatos.utils.DatabaseUtils

class AddProductAdapter(var photos: MutableList<Photo> = mutableListOf(), private val lifecycleScope: LifecycleCoroutineScope):RecyclerView.Adapter<AddProductViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddProductViewHolder {
        val binding = ItemPhotoBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return AddProductViewHolder(binding)     }


    override fun onBindViewHolder(holder: AddProductViewHolder, position: Int) {


        val photox=photos[position]



        holder.bind(photox,lifecycleScope,this)

    }

    override fun getItemCount(): Int {
        return photos.size
    }

    fun removeProductAtPosition(position: Int) {
        photos.removeAt(position)
        notifyItemRemoved(position)
    }

    fun updateData(newPhotos: List<Photo>) {
        photos.clear()
        photos.addAll(newPhotos)
        notifyDataSetChanged()
    }

}
class AddProductViewHolder(private val binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root) {
    var Room = DatabaseUtils.getDatabase()

    fun bind(photo: Photo, lifecycleScope: LifecycleCoroutineScope, adapter: AddProductAdapter) {


        binding.titleImg.visibility = View.VISIBLE
        binding.titleImg.text = photo.title
        binding.ivPhoto.load(photo.url)

    }
}