package com.kaylr.superhero_room.bbdd

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HeroDao {
    @Query("SELECT * FROM hero_table")
    suspend fun getAllSuperheroes():List<HeroEntity>

    @Query("SELECT * FROM hero_table WHERE name LIKE :query")
    suspend fun getSuperheroes(query:String):List<HeroEntity>

    @Query("SELECT * FROM hero_table WHERE id LIKE :id")
    suspend fun getSuperheroes(id:Int):List<HeroEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(superheroes:List<HeroEntity>)

    @Query("DELETE FROM hero_table")
    suspend fun deleteAllSuperheroes()
}