package com.kaylr.superhero_room.bbdd

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kaylr.superhero_room.SuperheroIdResponse
import com.kaylr.superhero_room.SuperheroItemResponse

@Entity (tableName = "details_table")
data class DetailsEntity (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "intelligence") val intelligence: String?,
   /* @ColumnInfo(name = "strength") val strength: String?,
    @ColumnInfo(name = "speed") val speed: String?,
    @ColumnInfo(name = "durability") val durability: String?,
    @ColumnInfo(name = "power") val power: String?,
    @ColumnInfo(name = "combat") val combat: String?,*/
    @ColumnInfo(name = "full-name") val fullName: String?,
    @ColumnInfo(name = "publisher") val publisher: String?
)
fun SuperheroIdResponse.toDatabase() = DetailsEntity(intelligence = powerstats.intelligence, fullName = biography.fullName, publisher = biography.publisher)