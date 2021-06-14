package com.example.flickrphotos.presentation.photos

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.flickrphotos.R
import com.example.flickrphotos.databinding.ActivityPhotoDetailsBinding
import com.example.flickrphotos.entities.PhotoModel
import com.example.flickrphotos.presentation.photos.PhotoListActivity.Companion.BUNDLE
import com.example.flickrphotos.presentation.photos.PhotoListActivity.Companion.PHOTO_OBJECT

class PhotoDetailsActivity : AppCompatActivity() {

    lateinit var binding: ActivityPhotoDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_photo_details)

        intent?.getBundleExtra(BUNDLE)
            ?.getParcelable<PhotoModel>(PHOTO_OBJECT)?.apply {
                val photo = this
                photo.page = 0
                binding.apply {
                    photoModel = photo
                    executePendingBindings()
                }
            }
    }
}