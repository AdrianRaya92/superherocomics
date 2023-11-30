package com.ayardreams.data

import com.ayardreams.data.datasource.ComicsLocalDataSource
import com.ayardreams.data.datasource.ComicsRemoteDataSource
import com.ayardreams.domain.Error
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
        if (localDataSource.isEmpty()) {
            val marvelComics = remoteDataSource.findMarvelComics(dateRange, offset)
            marvelComics.fold(ifLeft = { return it }) {
                localDataSource.save(it)
            }
        } else if (!localDataSource.isEmpty() && localDataSource.getFirstCurrentDate() != currentDate) {
            localDataSource.deleteAllComics()
            val marvelComics = remoteDataSource.findMarvelComics(dateRange, offset)
            marvelComics.fold(ifLeft = { return it }) {
                localDataSource.save(it)
            }
        }
        return null
    }
}
