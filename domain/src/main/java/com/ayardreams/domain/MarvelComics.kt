package com.ayardreams.domain

data class MarvelComics(
    val id: Int,
    val title: String,
    val resume: String?,
    val modified: String,
    val pageCount: Int,
    val thumbnail: String,
    val currentDate: String
)
