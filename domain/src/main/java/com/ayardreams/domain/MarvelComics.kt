package com.ayardreams.domain

data class MarvelComics(
    val id: Int,
    val title: String,
    val resume: String?,
    val modified: String,
    val issueNumber: String,
    val price: String,
    val pageCount: String,
    val thumbnail: String,
    val currentDate: String
)
