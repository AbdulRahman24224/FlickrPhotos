package com.example.flickrphotos.domain.usecases

import androidx.lifecycle.MutableLiveData
import com.example.flickrphotos.domain.database.appDatabase
import com.example.flickrphotos.domain.repositories.PhotosRepository
import com.example.flickrphotos.domain.repositories.photsRepository
import com.example.flickrphotos.entities.PhotoModel


typealias PhotosResult = MutableLiveData<MutableList<PhotoModel?>>

class RetrieveReposUseCase(
    private val repository: PhotosRepository = photsRepository,
    private val retrieving: MutableLiveData<Boolean>,
    private val result: PhotosResult,
    private val pageLD: MutableLiveData<Int>,
    private val snackLD: MutableLiveData<String>

) {

    operator fun invoke(connected: Boolean, isDBEmpty: Boolean = repository.isDatabaseEmpty()) {

        //choosing source of data based on network state and cached data
       if (connected) retrieveFromServer(pageLD.value?:0)
       else if (!(!connected and isDBEmpty)) retrieveFromDatabase(pageLD.value?:0)

    }

    private fun retrieveFromServer(page:Int) {
        val retrievingState = retrieving.value ?: true
        // if not already retrieving retrieve data from server
        // and update cached data
        retrievingState
               .takeUnless {  it }
                ?.also { retrieving.postValue(true) }
                ?.also { if (page==1) clearReposTable() }
                ?.let { repository.retrieveReposListFromServer(page).blockingGet() }
                ?.also { result.apply { value?.addAll(it).apply { postValue(value) } }}
                ?.onEach { it?.page = page }
                ?.also { repository.saveReposListToDatabase(it) }
                ?.also { retrieving.postValue(false) }
                ?.also { page.plus(1).apply { pageLD.postValue(this)} }

    }

    private fun retrieveFromDatabase(page:Int) {
        val retrievingState = retrieving.value ?: true
        retrievingState
            .takeUnless { it }
            ?.also { retrieving.postValue(true) }
            ?.let { repository.retrieveReposListFromDatabase(page).blockingGet() }
            .also { result.postValue(it) }
            .also { retrieving.postValue(false) }
            .also { snackLD.postValue("This is Offline Data , Please Check your Internet" +
                    " Connection and Refresh to get Latest Data") }


    }
}


fun clearReposTable() {
    appDatabase.photosDao.clearAll()
}
