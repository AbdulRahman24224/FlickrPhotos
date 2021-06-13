package com.example.agh.pstask.domain.database


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import io.reactivex.Single


@Dao
interface ReposDao{
    @Query( "  select * from Repos ORDER BY name COLLATE NOCASE ASC   ")
    fun queryAll(): Single<MutableList<Repos?>>

    @Query( "  select count(*) from Repos ")
    fun retrieveRepoCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun saveAll(objects: MutableList<Repos?>)


    @Query("DELETE FROM Repos ")
    fun clearAll()


}