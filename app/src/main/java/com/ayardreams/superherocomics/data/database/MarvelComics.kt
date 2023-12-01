package com.ayardreams.superherocomics.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MarvelComics(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val resume: String?,
    val modified: String,
    val issueNumber: String,
    val price: String,
    val pageCount: String,
    val thumbnail: String,
    val currentDate: String
)