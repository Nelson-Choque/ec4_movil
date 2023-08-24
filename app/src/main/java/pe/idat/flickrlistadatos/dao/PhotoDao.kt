package pe.idat.flickrlistadatos.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import pe.idat.flickrlistadatos.model.Photo

@Dao
interface PhotoDao {


    @Query("SELECT * FROM photos")
    suspend fun getAllProducts(): List<Photo>

    @Insert
    suspend fun agregarUsuario(producto: Photo)

    @Update
    suspend fun actualizarProducto(producto: Photo)

    @Query("DELETE FROM photos")
    suspend fun borrarTodosLosProductos()

    @Query("SELECT * FROM photos where id=:id")
    suspend fun getProductById(id: String): Photo

    @Query("SELECT * FROM photos where url=:image")
    suspend fun getProductByImage(image: String): Photo
}