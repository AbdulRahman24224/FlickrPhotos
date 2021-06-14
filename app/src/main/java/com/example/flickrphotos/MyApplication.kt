package com.example.flickrphotos

import android.app.Application
import com.example.flickrphotos.domain.Domain


class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Domain.integrateWith(this)
    }
}