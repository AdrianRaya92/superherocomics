package com.ayardreams.usecase

import com.ayardreams.data.ComicsRepository
import com.ayardreams.domain.MarvelComics
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FindComicUseCase @Inject constructor(private val repository: ComicsRepository) {

    operator fun invoke(id: Int): Flow<MarvelComics> = repository.findById(id)
}
