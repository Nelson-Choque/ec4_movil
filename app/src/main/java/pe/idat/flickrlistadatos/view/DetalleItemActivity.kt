package pe.idat.flickrlistadatos.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import pe.idat.flickrlistadatos.FavoriteFragment
import pe.idat.flickrlistadatos.R
import pe.idat.flickrlistadatos.databinding.ActivityDetalleItemBinding
import pe.idat.flickrlistadatos.databinding.FragmentFavoriteBinding
import pe.idat.flickrlistadatos.model.Photo
import pe.idat.flickrlistadatos.utils.DatabaseUtils
import pe.idat.flickrlistadatos.viewmodel.FavoriteViewModel

class DetalleItemActivity : AppCompatActivity() {

    lateinit var binding: ActivityDetalleItemBinding
    lateinit var fragment: FragmentFavoriteBinding
    private val favoriteViewModel: FavoriteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleItemBinding.inflate(layoutInflater)


        setContentView(binding.root)

        val item=intent.getParcelableExtra<Photo>("itemx")

        if (item!=null){
            val title: TextView =findViewById(R.id.tvTittleItem)
            val imgx: ImageView =findViewById(R.id.ivDetalleX)
            val id: TextView =findViewById(R.id.tvIdItem)
            title.text=item.title
            Glide.with(this).load(item.url)
                .into(imgx)

            id.text=item.id

        }

        lifecycleScope.launch {
            var Room = DatabaseUtils.getDatabase()


            var product = Room.productDao().getProductById(item!!.id);

            if(product!=null){
                if(product!!.isFavorite){
                    binding.button.text = "Eliminar de favoritos"


                }
            }


        }

        binding.button.setOnClickListener{


            lifecycleScope.launch {
                var Room = DatabaseUtils.getDatabase()


                var product = Room.productDao().getProductById(item!!.id);

                if (product != null) {

                    if (product.isFavorite) {

                        item.isFavorite = false
                        Room.productDao().actualizarProducto(item)

                        val message = "Este producto se quito de favoritos"
                        val duration = Snackbar.LENGTH_SHORT

                        val toast = Snackbar.make(binding.root, message, duration)

                        toast.show()
                        binding.button.text = "Añadir de favoritos"

                        favoriteViewModel.removeFromFavorites(item)



                        println("entreaaaaaaa")

                    } else {

                        item.isFavorite = true
                        Room.productDao().actualizarProducto(item)

                        val message = "se añadio a favoritos"

                        val duration = Snackbar.LENGTH_SHORT

                        val toast = Snackbar.make(binding.root, message, duration)
                        toast.show()

                        binding.button.text = "Eliminar de favoritos"


                    }

                } else {

                    item.isFavorite = true
                    Room.productDao().agregarUsuario(item)

                    val message = "se añadio a favoritos"

                    val duration = Snackbar.LENGTH_SHORT

                    val toast = Snackbar.make(binding.root, message, duration)
                    toast.show()

                    binding.button.text = "Eliminar de favoritos"




                }
            }

        }

    }


}