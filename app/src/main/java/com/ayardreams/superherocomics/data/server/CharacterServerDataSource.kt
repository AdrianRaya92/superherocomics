package com.ayardreams.superherocomics.data.server

import arrow.core.Either
import com.ayardreams.data.datasource.CharacterRemoteDataSource
import com.ayardreams.domain.Error
import com.ayardreams.domain.MarvelApi
import com.ayardreams.domain.MarvelCharacter
import com.ayardreams.superherocomics.data.tryCall
import com.ayardreams.superherocomics.di.ApiHash
import com.ayardreams.superherocomics.di.ApiKey
import com.ayardreams.superherocomics.di.ApiTs
import javax.inject.Inject

class CharacterServerDataSource @Inject constructor(
    @ApiKey private val apiKey: String,
    @ApiTs private val apiTs: String,
    @ApiHash private val apiHash: String,
    private val remoteService: MarvelService
) :
    CharacterRemoteDataSource {

    override suspend fun findMarvelCharacters(offset: Int): Either<Error, List<MarvelCharacter>> = tryCall {
        remoteService
            .listMarvelCharacters(apiKey, apiTs, apiHash, MarvelApi.limit, offset)
            .data.results
            .toDomainModel()
    }
}

private fun List<RemoteCharacterResult>.toDomainModel(): List<MarvelCharacter> =
    map { it.toDomainModel() }

private fun RemoteCharacterResult.toDomainModel(): MarvelCharacter =
    MarvelCharacter(
        id,
        name,
        description,
        modified,
        resourceURI,
        "${thumbnail.path}/standard_fantastic.${thumbnail.extension}".replace("http", "https")
    )
