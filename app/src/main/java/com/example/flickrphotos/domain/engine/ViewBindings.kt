package com.example.flickrphotos.domain.engine

import android.annotation.SuppressLint
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.flickrphotos.R
import com.example.flickrphotos.entities.PhotoModel

object ViewBindingAdapters {
    @SuppressLint("CheckResult")
    @JvmStatic
    @BindingAdapter("bind_image")
    fun bindImage(
        imageView: AppCompatImageView,
        photoModel: PhotoModel
    ) {

        val imagePath =
            photoModel.let { "http://farm${it.farm}.static.flickr.com/${it.server}/${it.id}_${it.secret}.jpg" }

        Glide.with(imageView).load(imagePath).diskCacheStrategy(DiskCacheStrategy.ALL).apply {
            RequestOptions.placeholderOf(R.mipmap.ic_launcher)
            if (photoModel.page != 0) override(500, 500)
        }
            .error(R.drawable.ic_error_notification)
            .centerInside()
            .into(imageView)
    }

}