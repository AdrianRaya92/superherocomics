package com.ayardreams.usecase

import com.ayardreams.data.ComicsRepository
import javax.inject.Inject

class GetComicsUseCase @Inject constructor(private val repository: ComicsRepository) {

    operator fun invoke() = repository.comicsMarvel
}