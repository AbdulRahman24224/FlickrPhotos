package com.example.flickrphotos.domain.repositories

import com.example.flickrphotos.domain.PhotosService
import com.example.flickrphotos.domain.database.AppDatabase
import com.example.flickrphotos.domain.database.appDatabase
import com.example.flickrphotos.domain.photosService

import com.example.flickrphotos.entities.PhotoModel


val photosRepository: PhotosRepository by lazy { PhotosRepository() }

class PhotosRepository(
    private val photoService: PhotosService = photosService,
    private val database: AppDatabase = appDatabase
) {

    fun retrieveReposListFromServer(page: Int) = photoService.retrievePhotosList(page = page)
    fun retrieveReposListFromDatabase(page: Int) = database.photosDao.retrieveByPage(page)
    fun isDatabaseEmpty() = database.photosDao.retrieveRepoCount() <= 0
    fun saveReposListToDatabase(repos: MutableList<PhotoModel?>) =
        database.photosDao.insertAll(repos)

    fun clearRepos() = database.photosDao.clearAll()

}