package com.example.flickrphotos.domain.repositories


import com.example.flickrphotos.domain.PhotosService
import com.example.flickrphotos.domain.database.AppDatabase
import com.example.flickrphotos.domain.database.appDatabase
import com.example.flickrphotos.domain.photosService

import com.example.flickrphotos.entities.PhotoModel


val photsRepository: PhotosRepository by lazy { PhotosRepository() }

class PhotosRepository(
    private val photoService: PhotosService = photosService,
    private val database: AppDatabase = appDatabase
)   {

     fun retrieveReposListFromServer(page :Int) = photoService.retrievePhotosList(page = page)
     fun retrieveReposListFromDatabase() = database.reposDao.queryAll()
     fun isDatabaseEmpty()= database.reposDao.retrieveRepoCount() <= 0
     fun saveReposListToDatabase(repos: MutableList<PhotoModel?>) = database.reposDao.saveAll(repos)
     fun clearRepos() = database.reposDao.clearAll()

}