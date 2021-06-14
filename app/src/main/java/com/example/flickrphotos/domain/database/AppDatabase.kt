package com.example.flickrphotos.domain.database


import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.agh.pstask.domain.database.PhotosDao
import com.example.flickrphotos.domain.applicationLiveData
import com.example.flickrphotos.domain.getApplication
import com.example.flickrphotos.entities.PhotoModel


val appDatabase by lazy {

    Room.databaseBuilder(
        applicationLiveData.getApplication(),
        AppDatabase::class.java, "database"
    ).fallbackToDestructiveMigration()
        .build()

}

@Database(
    entities = [PhotoModel::class], version = 2, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract val photosDao: PhotosDao
}