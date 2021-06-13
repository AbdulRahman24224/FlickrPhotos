package com.example.flickrphotos.entities

import android.support.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class PhotoModel(

    @field:NonNull
    @field:PrimaryKey
    val id: Int?,
    val farm: Int,
    val isfamily: Int,
    val isfriend: Int,
    val ispublic: Int,
    val owner: String,
    val secret: String,
    val server: String,
    val title: String ,
    val page: Int?=null
) : Serializable