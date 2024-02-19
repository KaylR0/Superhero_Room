package com.kaylr.superhero_room.bbdd

import androidx.room.Database
import androidx.room.RoomDatabase
@Database(entities = [HeroEntity::class, DetailsEntity::class], version = 1)
abstract class SuperheroDatabase: RoomDatabase() {
    abstract fun getHeroDao(): HeroDao
    abstract fun getDetailsDao(): DetailsDao
}