package pe.idat.flickrlistadatos.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import coil.load
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import pe.idat.flickrlistadatos.R
import pe.idat.flickrlistadatos.databinding.ItemPhotoBinding
import pe.idat.flickrlistadatos.model.Photo
import pe.idat.flickrlistadatos.utils.DatabaseUtils

class FavoriteAdapter(var photos: MutableList<Photo> = mutableListOf(), private val lifecycleScope: LifecycleCoroutineScope): RecyclerView.Adapter<FavoriteViewHolder>(){

    var onItemClick:((Photo)-> Unit)?=null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteViewHolder {
        val binding = ItemPhotoBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return FavoriteViewHolder(binding)     }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {


        val photox=photos[position]

        holder.itemView.setOnClickListener{
            onItemClick?.invoke(photox)
        }

        holder.bind(photox,lifecycleScope,this)

   }

    override fun getItemCount(): Int {
        return photos.size
    }

    fun removeProductAtPosition(position: Int) {
        photos.removeAt(position)
        println("me ejecute ")
        notifyItemRemoved(position)
    }


}
class FavoriteViewHolder(private val binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root) {
    var Room = DatabaseUtils.getDatabase()

    fun bind(photo: Photo, lifecycleScope: LifecycleCoroutineScope, adapter: FavoriteAdapter) {

        binding.ivPhoto.load( photo.url)


        if(photo != null){

            if(photo.isFavorite){
                binding.favoriteButton.setImageResource(R.drawable.baseline_favorite_24_red)

            }


        }

        binding.favoriteButton.setOnClickListener{
            lifecycleScope.launch {

                var product = Room.productDao().getProductById(photo.id);

                var photoiD = Room.productDao().getProductById(photo.id);

                if(photoiD.isFavorite){

                    photoiD.isFavorite = false

                    Room.productDao().actualizarProducto(photoiD)


                    val position = adapter.photos.indexOf(photo)
                    if (position != -1) {
                        adapter.removeProductAtPosition(position)
                    }

                }



            }
        }


    }
}