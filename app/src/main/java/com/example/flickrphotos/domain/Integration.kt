package com.example.flickrphotos.domain

import android.app.Application
import androidx.lifecycle.MutableLiveData

// holding application context in Domain Layer
// after integrating it with presentation Layer
internal val applicationLiveData = MutableLiveData<Application>()

internal fun MutableLiveData<Application>.getApplication() = value!!

object Domain {
    fun integrateWith(application: Application) {
        applicationLiveData.value = application
    }
}