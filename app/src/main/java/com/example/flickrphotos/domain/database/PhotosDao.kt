package com.example.agh.pstask.domain.database


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.flickrphotos.entities.PhotoModel

import io.reactivex.Single


@Dao
interface PhotosDao {

    @Query("  select * from PhotoModel Where page = :pageNumber   ")
    fun retrieveByPage(pageNumber: Int): Single<MutableList<PhotoModel?>>

    @Query("  select count(*) from PhotoModel ")
    fun retrieveRepoCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun insertAll(objects: MutableList<PhotoModel?>)


    @Query("DELETE FROM PhotoModel ")
    fun clearAll()


}