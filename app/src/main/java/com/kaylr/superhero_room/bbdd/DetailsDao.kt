package com.kaylr.superhero_room.bbdd

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

interface DetailsDao {
    @Query("SELECT * FROM details_table")
    suspend fun getAllDetails():List<DetailsEntity>

    @Query("SELECT * FROM details_table WHERE `full-name` LIKE :query")
    suspend fun getDetails(query:String):List<DetailsEntity>

    @Query("SELECT * FROM details_table WHERE id LIKE :id")
    suspend fun getDetails(id:Int):List<DetailsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(details:List<DetailsEntity>)

    @Query("DELETE FROM details_table")
    suspend fun deleteAllDetails()
}