package pe.idat.flickrlistadatos.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.Lifecycling
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import pe.idat.flickrlistadatos.R
import pe.idat.flickrlistadatos.databinding.ItemPhotoBinding
import pe.idat.flickrlistadatos.model.Photo
import pe.idat.flickrlistadatos.utils.DatabaseUtils

class PhotosAdapter(private val lifecycleScope: LifecycleCoroutineScope,val photos: MutableList<Photo> = mutableListOf()):RecyclerView.Adapter<PhotosAdapter.PhotosViewHolder>() {

    var onItemClick:((Photo)-> Unit)?=null


    class PhotosViewHolder(val binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(photo: Photo, lifecycleScope: LifecycleCoroutineScope, photosAdapter: PhotosAdapter,position: Int) {


            println("mi url es:"+ photo.url)



            println(photo.title)



            lifecycleScope.launch {

                var room = DatabaseUtils.getDatabase()

                var product = room.productDao().getProductByImage(photo.url);

                println("encontre: " +product)

                if(product != null){

                    if(product.isFavorite){
                        println("entreee")
                        binding.favoriteButton.setImageResource(R.drawable.baseline_favorite_24_red)

                    }
                    else{
                        binding.favoriteButton.setImageResource(R.drawable.baseline_favorite_24)

                    }

                }
                else{
                    binding.favoriteButton.setImageResource(R.drawable.baseline_favorite_24)

                }
            }


            binding.favoriteButton.setOnClickListener{

                lifecycleScope.launch {
                    var Room = DatabaseUtils.getDatabase()

                    println("estoy en la imagen" + position)

                    var product = Room.productDao().getProductById(photo.id);

                    if(product != null){

                        if(product.isFavorite) {

                            val message = "Este producto ya esta añadido en favoritos"
                            val duration = Snackbar.LENGTH_SHORT

                            val toast = Snackbar.make(binding.root, message, duration)
                            toast.show()
                        }else{

                            photo.isFavorite = true
                            Room.productDao().actualizarProducto(photo)

                            binding.favoriteButton.setImageResource(R.drawable.baseline_favorite_24_red)

                            val message = "se añadio a favoritos"

                            val duration = Snackbar.LENGTH_SHORT

                            val toast = Snackbar.make(binding.root, message, duration)
                            toast.show()

                            photosAdapter.notifyDataSetChanged();
                        }

                    }
                    else{

                        photo.isFavorite = true
                        Room.productDao().agregarUsuario(photo)

                        binding.favoriteButton.setImageResource(R.drawable.baseline_favorite_24_red)

                        photosAdapter.notifyDataSetChanged();


                    }



                }

            }

            Glide.with(itemView.context).load(photo.url)
                .into(binding.ivPhoto)

        }


    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        val binding=ItemPhotoBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PhotosViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {
        val photox=photos[position]

        holder.itemView.setOnClickListener{
            onItemClick?.invoke(photox)
        }

        holder.bind(photox,lifecycleScope,this,position)



    }

    override fun getItemCount()=photos.size


}


