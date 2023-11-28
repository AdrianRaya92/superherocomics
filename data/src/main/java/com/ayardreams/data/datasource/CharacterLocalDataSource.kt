package com.ayardreams.data.datasource

import com.ayardreams.domain.MarvelCharacter
import com.ayardreams.domain.Error
import kotlinx.coroutines.flow.Flow

interface CharacterLocalDataSource {
    val characters: Flow<List<MarvelCharacter>>

    suspend fun isEmpty(): Boolean
    fun findById(id: Int): Flow<MarvelCharacter>
    suspend fun save(marvelCharacters: List<MarvelCharacter>): Error?
}