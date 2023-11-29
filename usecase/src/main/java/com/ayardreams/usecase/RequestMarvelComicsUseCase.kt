package com.ayardreams.usecase

import com.ayardreams.data.ComicsRepository
import com.ayardreams.domain.Error
import javax.inject.Inject

class RequestMarvelComicsUseCase @Inject constructor(private val repository: ComicsRepository) {

    suspend operator fun invoke(currentDate: String, dateRange: String, offset: Int): Error? {
        return repository.requestMarvelComics(currentDate, dateRange, offset)
    }
}