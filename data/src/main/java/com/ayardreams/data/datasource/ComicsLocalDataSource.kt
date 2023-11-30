package com.ayardreams.data.datasource

import com.ayardreams.domain.Error
import com.ayardreams.domain.MarvelComics
import kotlinx.coroutines.flow.Flow

interface ComicsLocalDataSource {
    val comics: Flow<List<MarvelComics>>

    suspend fun getFirstCurrentDate(): String
    suspend fun isEmpty(): Boolean
    suspend fun getComicsTotal(): Int
    fun findById(id: Int): Flow<MarvelComics>
    suspend fun save(marvelComics: List<MarvelComics>): Error?
    suspend fun deleteAllComics()
}
