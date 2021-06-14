package com.example.flickrphotos.presentation.photos

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.flickrphotos.domain.engine.ConnectivityUtil
import com.example.flickrphotos.domain.engine.logDebug
import com.example.flickrphotos.domain.engine.logError
import com.example.flickrphotos.domain.engine.toMutableLiveData
import com.example.flickrphotos.domain.repositories.PhotosRepository
import com.example.flickrphotos.domain.repositories.photosRepository
import com.example.flickrphotos.domain.usecases.PhotosResult

import com.example.flickrphotos.domain.usecases.RetrieveReposUseCase
import com.example.flickrphotos.domain.usecases.clearPhotosTable
import com.example.flickrphotos.entities.PhotoModel

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject


class PhotosViewModel(

    val retrieveProgress: MutableLiveData<Boolean> = false.toMutableLiveData(),
    val ReposResult: PhotosResult = mutableListOf<PhotoModel?>().toMutableLiveData(),
    val toastText: PublishSubject<String> = PublishSubject.create(),
    val snackLD: MutableLiveData<String> = "".toMutableLiveData(),
    private val disposables: CompositeDisposable = CompositeDisposable(),
    private val repository: PhotosRepository = photosRepository,
    val pageLD: MutableLiveData<Int> = 1.toMutableLiveData(),
    private val retrieveReposUseCase: RetrieveReposUseCase = RetrieveReposUseCase(
        repository,
        retrieveProgress,
        ReposResult,
        pageLD,
        snackLD
    )
) : ViewModel() {

    fun retrievePhotos(context: Context) {
        val networkState = ConnectivityUtil.isConnectionOn(context) ?: false

        if (!networkState) toastText.onNext("Please Check Your Internet Connection")
        Observable.fromCallable { retrieveReposUseCase(networkState) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ "retrieved successfully".logDebug() }, {
                it.message?.apply {
                    logError()
                    snackLD.postValue("Couldn't Retrieve Data")
                }
                retrieveProgress.postValue(false)
            })
            .also { disposables.add(it) }
    }

    fun clearPhotos() {
        Observable.fromCallable { clearPhotosTable() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ "cleared successfully".logDebug() }, {
                it.message?.logError()
                retrieveProgress.postValue(false)
            })
            .also { disposables.add(it) }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()

    }


}