package com.ayardreams.superherocomics.data.database

import com.ayardreams.data.datasource.ComicsLocalDataSource
import com.ayardreams.domain.Error
import com.ayardreams.domain.MarvelComics
import com.ayardreams.superherocomics.data.tryCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.ayardreams.superherocomics.data.database.MarvelComics as DbComics

class ComicsRoomDataSource @Inject constructor(private val comicsDao: ComicsDao) :
    ComicsLocalDataSource {

    override val comics: Flow<List<MarvelComics>> = comicsDao.getAll().map { it.toDomainModel() }

    override suspend fun isEmpty(): Boolean = comicsDao.marvelComicsCount() == 0

    override suspend fun getFirstCurrentDate(): String = comicsDao.getFirstCurrentDate() ?: ""

    override fun findById(id: Int): Flow<MarvelComics> = comicsDao.findById(id).map { it.toDomainModel() }

    override suspend fun save(marvelComics: List<MarvelComics>): Error? = tryCall {
        comicsDao.insertMarvelComics(marvelComics.fromDomainModel())
    }.fold(
        ifLeft = { it },
        ifRight = { null }
    )

    override suspend fun deleteAllComics() {
        comicsDao.deleteAll()
    }
}

private fun List<DbComics>.toDomainModel(): List<MarvelComics> = map { it.toDomainModel() }

private fun DbComics.toDomainModel(): MarvelComics =
    MarvelComics(
        id,
        title,
        resume,
        modified,
        pageCount,
        thumbnail,
        currentDate
    )

private fun List<MarvelComics>.fromDomainModel(): List<DbComics> = map { it.fromDomainModel() }

private fun MarvelComics.fromDomainModel(): DbComics = DbComics(
    id,
    title,
    resume,
    modified,
    pageCount,
    thumbnail,
    currentDate
)
