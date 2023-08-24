package pe.idat.flickrlistadatos.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pe.idat.flickrlistadatos.model.Photo
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pe.idat.flickrlistadatos.retrofit.Cliente

class PhotoViewModel:ViewModel() {
    private val mutableLiveDataPhoto=MutableLiveData<List<Photo>>()

    val photsLiveData:LiveData<List<Photo>> = mutableLiveDataPhoto

    fun searchPhotosByTerm(searchTerm: String) {
        viewModelScope.launch {
            val searchResponse = Cliente.client.searchPhotosByTerm(searchTerm)
            val photoList = searchResponse.photos.photo.map { photo ->
                Photo(
                    id = photo.id,
                    url = "https://farm${photo.farm}.staticflickr.com/${photo.server}/${photo.id}_${photo.secret}.jpg",
                    title = photo.title
                )
            }

            mutableLiveDataPhoto.postValue(photoList)
        }
    }

}
