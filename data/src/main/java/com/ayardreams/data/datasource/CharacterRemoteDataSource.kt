package com.ayardreams.data.datasource

import arrow.core.Either
import com.ayardreams.domain.Error
import com.ayardreams.domain.MarvelCharacter

interface CharacterRemoteDataSource {
    suspend fun findMarvelCharacters(offset: Int): Either<Error, List<MarvelCharacter>>
}
