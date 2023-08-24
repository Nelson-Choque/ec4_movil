package pe.idat.flickrlistadatos.utils

import android.content.Context
import androidx.room.Room
import pe.idat.flickrlistadatos.room.AppDatabase

object DatabaseUtils {

    private lateinit var room: AppDatabase

    fun initDatabase(context: Context) {
        room = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "photos").fallbackToDestructiveMigration().build()
    }

    fun getDatabase(): AppDatabase {
        return room
    }
}