package com.ayardreams.data

import com.ayardreams.data.datasource.CharacterLocalDataSource
import com.ayardreams.data.datasource.CharacterRemoteDataSource
import com.ayardreams.domain.Error
import com.ayardreams.domain.MarvelCharacter
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CharactersRepository @Inject constructor(
    private val localDataSource: CharacterLocalDataSource,
    private val remoteDataSource: CharacterRemoteDataSource
) {
    val charactersMarvel get() = localDataSource.characters

    fun findById(id: Int): Flow<MarvelCharacter> = localDataSource.findById(id)

    suspend fun requestMarvelCharacters(offset: Int): Error? {
        if (localDataSource.isEmpty()) {
            val movies = remoteDataSource.findMarvelCharacters(offset)
            movies.fold(ifLeft = { return it }) {
                localDataSource.save(it)
            }
        }
        return null
    }
}