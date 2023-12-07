package com.ayardreams.data

import com.ayardreams.data.datasource.ComicsLocalDataSource
import com.ayardreams.data.datasource.ComicsRemoteDataSource
import com.ayardreams.domain.Error
import com.ayardreams.domain.MarvelApi
import com.ayardreams.domain.MarvelComics
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ComicsRepository @Inject constructor(
    private val localDataSource: ComicsLocalDataSource,
    private val remoteDataSource: ComicsRemoteDataSource
) {
    val comicsMarvel get() = localDataSource.comics

    fun findById(id: Int): Flow<MarvelComics> = localDataSource.findById(id)

    suspend fun comicsTotal(): Int = localDataSource.getComicsTotal()

    suspend fun requestMarvelComics(currentDate: String, dateRange: String, offset: Int): Error? {
        val findComics = when {
            localDataSource.isEmpty() -> true
            offset % MarvelApi.limit == 0 && offset != 0 -> true
            localDataSource.getFirstCurrentDate() != currentDate -> {
                localDataSource.deleteAllComics()
                true
            }
            else -> false
        }

        if (findComics) {
            val marvelComics = remoteDataSource.findMarvelComics(dateRange, offset)
            return marvelComics.fold(ifLeft = { it }) {
                localDataSource.save(it)
                null
            }
        }
        return null
    }
}
