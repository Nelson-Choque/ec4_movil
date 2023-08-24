package pe.idat.flickrlistadatos

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import pe.idat.flickrlistadatos.adapter.FavoriteAdapter
import pe.idat.flickrlistadatos.databinding.FragmentFavoriteBinding
import pe.idat.flickrlistadatos.databinding.FragmentListaBinding
import pe.idat.flickrlistadatos.model.Photo
import pe.idat.flickrlistadatos.utils.DatabaseUtils
import pe.idat.flickrlistadatos.view.DetalleItemActivity
import pe.idat.flickrlistadatos.viewmodel.FavoriteViewModel
import pe.idat.flickrlistadatos.viewmodel.PhotoViewModel


class FavoriteFragment : Fragment() {

    lateinit var binding: FragmentFavoriteBinding
    private lateinit var adapter: FavoriteAdapter
    val room = DatabaseUtils.getDatabase()
    private val favoriteViewModel: FavoriteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {


        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentFavoriteBinding.inflate(inflater,container,false)







        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val productosfavoritos = mutableListOf<Photo>()

        lifecycleScope.launch {

            adapter = FavoriteAdapter(productosfavoritos,lifecycleScope)
            binding.rvListaDatos.adapter = adapter
            binding.rvListaDatos.layoutManager = LinearLayoutManager(requireContext())


            favoriteViewModel.favoritePhotos.observe(viewLifecycleOwner,
                Observer {
                        list:List<Photo>->
                    with(adapter){
                        photos.clear()
                        photos.addAll(list)
                        notifyDataSetChanged()


                    }
                })

            adapter.onItemClick={
                val intentx= Intent(requireContext(), DetalleItemActivity::class.java)
                intentx.putExtra("itemx",it)


                startActivityForResult(intentx,1)

            }


            favoriteViewModel.getFavoritos()

        }
    }
    override fun onResume() {
        super.onResume()

        favoriteViewModel.favoritePhotos.observe(viewLifecycleOwner) { list ->
            adapter.photos.clear()
            adapter.photos.addAll(list)
            adapter.notifyDataSetChanged()
        }

        favoriteViewModel.getFavoritos()
    }


}
