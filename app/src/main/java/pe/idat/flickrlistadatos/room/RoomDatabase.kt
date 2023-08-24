package pe.idat.flickrlistadatos.room

import androidx.room.Database
import androidx.room.RoomDatabase
import pe.idat.flickrlistadatos.dao.PhotoDao
import pe.idat.flickrlistadatos.model.Photo

@Database(entities = [Photo::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): PhotoDao

}