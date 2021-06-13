package com.example.flickrphotos.domain.database

import android.arch.persistence.room.*
import androidx.room.Database
import com.example.agh.pstask.domain.database.PhotosDao
import com.example.flickrphotos.domain.applicationLiveData
import com.example.flickrphotos.domain.getApplication
import com.example.flickrphotos.entities.PhotoModel


val appDatabase by lazy {

    Room.databaseBuilder(
            applicationLiveData.getApplication(),
            AppDatabase::class.java, "database"
    ).build()

}

@Database(entities = [PhotoModel::class ]
, version = 1  , exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
    abstract val photosDao: PhotosDao
}