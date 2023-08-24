package pe.idat.flickrlistadatos.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pe.idat.flickrlistadatos.model.Photo
import pe.idat.flickrlistadatos.utils.DatabaseUtils

class FavoriteViewModel:  ViewModel() {
    private val _favoritePhotos = MutableLiveData<MutableList<Photo>>(mutableListOf())
    val favoritePhotos: LiveData<MutableList<Photo>> = _favoritePhotos
    val room = DatabaseUtils.getDatabase()

    fun getFavoritos(){
        viewModelScope.launch {

            var productos = room.productDao().getAllProducts()

            val productosfavoritos = mutableListOf<Photo>()

            for (item in productos) {

                if (item.isFavorite) {
                    productosfavoritos.add(item)

                }

            }
            _favoritePhotos.postValue(productosfavoritos)

        }
        }

    fun addToFavorites(photo: Photo) {
        _favoritePhotos.value?.add(photo)
        _favoritePhotos.value = _favoritePhotos.value
    }

    fun removeFromFavorites(photo: Photo) {
        println("me ejecute siuuuuuuu")
        _favoritePhotos.value?.remove(photo)
        _favoritePhotos.value = _favoritePhotos.value

        getFavoritos()
    }
}