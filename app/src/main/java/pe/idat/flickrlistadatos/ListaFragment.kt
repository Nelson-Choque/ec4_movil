package pe.idat.flickrlistadatos

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.launch
import pe.idat.flickrlistadatos.adapter.PhotosAdapter
import pe.idat.flickrlistadatos.databinding.FragmentListaBinding
import pe.idat.flickrlistadatos.model.Photo
import pe.idat.flickrlistadatos.utils.DatabaseUtils
import pe.idat.flickrlistadatos.view.DetalleItemActivity
import pe.idat.flickrlistadatos.viewmodel.PhotoViewModel


class ListaFragment : Fragment() {
    // TODO: Rename and change types of parameters

    lateinit var binding:FragmentListaBinding
    private val photosViewModel: PhotoViewModel by viewModels()
    private val photosAdapter= PhotosAdapter(lifecycleScope)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        DatabaseUtils.initDatabase(requireContext())


        binding= FragmentListaBinding.inflate(inflater,container,false)
        val rootView = binding.root

        binding.rvListaDatos.adapter=photosAdapter
        binding.rvListaDatos.layoutManager= GridLayoutManager(requireContext(),1)

        photosAdapter.onItemClick={
            val intentx= Intent(requireContext(), DetalleItemActivity::class.java)
            intentx.putExtra("itemx",it)
            startActivity(intentx)

        }

        binding.button.setOnClickListener {
            val searchTerm = binding.editText.text.toString()
            if (searchTerm.isNotEmpty()) {
                // Llama a la función del ViewModel para buscar fotos por el término de búsqueda
                photosViewModel.searchPhotosByTerm(searchTerm)
            }
        }

        return rootView




    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


    }

    override fun onResume() {
        super.onResume()
        photosViewModel.photsLiveData.observe(this.viewLifecycleOwner,
            Observer {
                    list:List<Photo>->
                with(photosAdapter){
                    photos.clear()
                    photos.addAll(list)
                    notifyDataSetChanged()


                }
            })


    }


}