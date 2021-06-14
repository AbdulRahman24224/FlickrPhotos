package com.example.flickrphotos.entities


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


data class PhotosResponse(
    val photos: PhotosResponseBody? = null
)

data class PhotosResponseBody(
    val page: Int? = null,
    val pages: Int? = null,
    val perpage: Int? = null,
    val total: Int? = null,
    val photo: MutableList<PhotoModel?>
)


@Parcelize
@Entity
data class PhotoModel(
    @field:PrimaryKey
    val id: Long? = null,
    val farm: Int? = null,
    val isfamily: Int? = null,
    val isfriend: Int? = null,
    val ispublic: Int? = null,
    val owner: String = "",
    val secret: String = "",
    val server: String = "",
    val title: String = "",
    var page: Int = 1
) : Parcelable