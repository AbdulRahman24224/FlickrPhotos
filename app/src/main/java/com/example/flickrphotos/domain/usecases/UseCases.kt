package com.example.flickrphotos.domain.usecases

import androidx.lifecycle.MutableLiveData
import com.example.flickrphotos.domain.database.appDatabase
import com.example.flickrphotos.domain.repositories.PhotosRepository
import com.example.flickrphotos.domain.repositories.photosRepository
import com.example.flickrphotos.entities.PhotoModel


typealias PhotosResult = MutableLiveData<MutableList<PhotoModel?>>

class RetrieveReposUseCase(
    private val repository: PhotosRepository = photosRepository,
    private val retrieving: MutableLiveData<Boolean>,
    private val result: PhotosResult,
    private val pageLD: MutableLiveData<Int>,
    private val snackLD: MutableLiveData<String>

) {

    operator fun invoke(connected: Boolean, isDBEmpty: Boolean = repository.isDatabaseEmpty()) {

        //choosing source of data based on network state and cached data
        if (connected) retrieveFromServer(pageLD.value ?: 0)
        else if (!(connected.not() and isDBEmpty)) retrieveFromDatabase(pageLD.value ?: 0)

    }

    private fun retrieveFromServer(page: Int) {
        val retrievingState = retrieving.value ?: true
        // if not already retrieving retrieve data from server
        // and update cached data
        retrievingState
            .takeUnless { it }
            ?.also { retrieving.postValue(true) }
            ?.also { if (page == 1) clearPhotosTable() }
            ?.let { repository.retrieveReposListFromServer(page).blockingGet() }
            .let { it?.photos }
            .let { body ->
                body?.photo?.forEach { it?.page = body.page ?: 0 }
                body?.photo
            }
            ?.also { result.apply { value?.addAll(it).apply { postValue(value) } } }
            ?.also { repository.saveReposListToDatabase(it) }
            ?.also { retrieving.postValue(false) }
            ?.also { page.plus(1).apply { pageLD.postValue(this) } }


    }

    private fun retrieveFromDatabase(page: Int) {
        val retrievingState = retrieving.value ?: true
        retrievingState
            .takeUnless { it }
            ?.also { retrieving.postValue(true) }
            ?.let { repository.retrieveReposListFromDatabase(page).blockingGet() }
            .also {
                result.apply {
                    if (it != null) {
                        value?.addAll(it).apply { postValue(value) }
                    }
                }
            }
            .also { retrieving.postValue(false) }
            .also {
                if (page == 1) snackLD.postValue(
                    "This is Offline Data , Please Check your Internet" +
                            " Connection and Refresh to get Latest Data"
                )
            }
            ?.also { page.plus(1).apply { pageLD.postValue(this) } }


    }
}


fun clearPhotosTable() {
    appDatabase.photosDao.clearAll()
}


