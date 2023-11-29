package com.ayardreams.data.datasource

import arrow.core.Either
import com.ayardreams.domain.Error
import com.ayardreams.domain.MarvelComics

interface ComicsRemoteDataSource {
    suspend fun findMarvelComics(dateRange: String, offset: Int): Either<Error, List<MarvelComics>>
}
