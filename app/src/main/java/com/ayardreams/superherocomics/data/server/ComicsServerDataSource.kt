package com.ayardreams.superherocomics.data.server

import arrow.core.Either
import com.ayardreams.data.datasource.ComicsRemoteDataSource
import com.ayardreams.domain.Error
import com.ayardreams.domain.MarvelApi
import com.ayardreams.domain.MarvelComics
import com.ayardreams.superherocomics.data.tryCall
import com.ayardreams.superherocomics.di.ApiHash
import com.ayardreams.superherocomics.di.ApiKey
import com.ayardreams.superherocomics.di.ApiTs
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class ComicsServerDataSource @Inject constructor(
    @ApiKey private val apiKey: String,
    @ApiTs private val apiTs: String,
    @ApiHash private val apiHash: String,
    private val remoteService: MarvelService
) :
    ComicsRemoteDataSource {

    override suspend fun findMarvelComics(dateRange: String, offset: Int): Either<Error, List<MarvelComics>> = tryCall {
        remoteService
            .listMarvelCharacters(apiKey, apiTs, apiHash, dateRange, MarvelApi.limit, offset)
            .data.results
            .toDomainModel()
    }
}

private fun List<RemoteComicsResult>.toDomainModel(): List<MarvelComics> =
    map { it.toDomainModel() }

private fun RemoteComicsResult.toDomainModel(): MarvelComics =
    MarvelComics(
        id,
        title,
        description,
        modified,
        pageCount,
        "${thumbnail.path}/standard_fantastic.${thumbnail.extension}".replace("http", "https"),
        getCurrentDateFormatted()
    )

private fun getCurrentDateFormatted(): String {
    val currentDate = LocalDate.now()
    return currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
}