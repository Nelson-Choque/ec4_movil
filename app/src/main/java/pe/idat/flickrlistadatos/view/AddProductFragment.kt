package pe.idat.flickrlistadatos.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pe.idat.flickrlistadatos.R
import pe.idat.flickrlistadatos.adapter.AddProductAdapter
import pe.idat.flickrlistadatos.adapter.PhotosAdapter
import pe.idat.flickrlistadatos.databinding.FragmentAddProductBinding
import pe.idat.flickrlistadatos.databinding.FragmentFavoriteBinding
import pe.idat.flickrlistadatos.model.Photo


class AddProductFragment : Fragment() {

    lateinit var binding: FragmentAddProductBinding
    private lateinit var firestore:FirebaseFirestore
    var firstCreated = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firestore = Firebase.firestore

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentAddProductBinding.inflate(inflater,container,false)

        val photosList = mutableListOf<Photo>() // Lista para almacenar los datos

        val adapter = AddProductAdapter(photosList, lifecycleScope)

        binding.button.setOnClickListener{

            val titulo = binding.titulo.text.toString()

            var isFavorite = binding.checkbox.isChecked

            var url = "https://source.unsplash.com/random/800x600\n"

            println(isFavorite)

            if (titulo.isNotEmpty()){

                addProductToFirestore(titulo, isFavorite, url,adapter )
            }
            else{
                val message = "Todos los campos son obligatorios"
                val duration = Snackbar.LENGTH_SHORT

                val toast = Snackbar.make(binding.root, message, duration)
                toast.show()
            }
        }

        //recycler view


        binding.rvListaPhotos.adapter = adapter
        binding.rvListaPhotos.layoutManager = LinearLayoutManager(requireContext())

        getDataFirestore(photosList, adapter) // Llama a la función para obtener los datos


        return binding.root
    }

    private fun getDataFirestore(photosList: MutableList<Photo>, adapter: AddProductAdapter){
        firestore.collection("photos").get().addOnSuccessListener {

            val photos = it.documents
            for(photo in photos){

                Log.d("photo",photo.id)
                val title = photo.getString("title")
                val isFavorite = photo.getBoolean("isFavorite")
                val url = photo.getString("url")

                val photoData  = Photo(photo.id,url.toString(),title.toString(), isFavorite!!)
                photosList.add(photoData)

            }

            if(firstCreated){
                firstCreated = false
                println("aqui estoy")
                adapter.notifyDataSetChanged()
            }
            else{
                adapter.updateData(photosList)

            }
        }
    }

    private fun addProductToFirestore(titulo: String,isFavorite: Boolean, url:String,adapter: AddProductAdapter) {

        val photo = hashMapOf<String,Any>(
            "title" to titulo,
            "isFavorite" to isFavorite,
            "url" to url
        )

        firestore.collection("photos").add(photo).addOnSuccessListener{
            val message = "Se agrego con exito"
            val duration = Snackbar.LENGTH_SHORT

            val toast = Snackbar.make(binding.root, message, duration)
            toast.show()

            getDataFirestore(mutableListOf(),adapter)

        }.addOnFailureListener {
            val message = "hubo un error al agregar"
            val duration = Snackbar.LENGTH_SHORT

            val toast = Snackbar.make(binding.root, message, duration)
            toast.show()

        }

    }
}