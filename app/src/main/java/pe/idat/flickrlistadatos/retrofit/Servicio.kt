package pe.idat.flickrlistadatos.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Servicio {

    @GET("?method=flickr.photos.search&api_key=d55239b00958bc26619a5c7025c5fee5&tags=cats&format=json&nojsoncallback=1")
    suspend fun listar():PhotosSearchResponse

    @GET("?method=flickr.photos.search&api_key=d55239b00958bc26619a5c7025c5fee5&format=json&nojsoncallback=1")
    suspend fun searchPhotosByTerm(@Query("tags") searchTerm: String): PhotosSearchResponse
}