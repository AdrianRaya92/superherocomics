package com.ayardreams.superherocomics.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MarvelCharacter(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val description: String,
    val modified: String,
    val resourceURI: String,
    val thumbnail: String
)