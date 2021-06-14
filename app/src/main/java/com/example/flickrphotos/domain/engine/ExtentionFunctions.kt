package com.example.flickrphotos.domain.engine


import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.example.flickrphotos.R
import com.google.android.material.snackbar.Snackbar


fun <T> T.toMutableLiveData(): MutableLiveData<T> {
    return MutableLiveData<T>()
        .also { it.value = this }
}

fun Any.logDebug() {
    Log.d(this::class.java.simpleName, this.toString())
}

fun Any.logError() {
    Log.e(this::class.java.simpleName, this.toString())
}

fun Any.toast(context: Context, length: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(
        context, this.toString(), length
    ).show()

fun Any.snack(view: View, length: Int = Snackbar.LENGTH_LONG) {

    Snackbar.make(view, this.toString(), length)
        .apply {
            this.view.setBackgroundColor(
                ContextCompat.getColor((view.context) as Activity, R.color.darkRed)
            )
        }
        .setActionTextColor(Color.WHITE)
        .setAction("Ok") {}.show()

}



