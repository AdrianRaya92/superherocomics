package com.ayardreams.usecase

import com.ayardreams.data.ComicsRepository
import javax.inject.Inject

class GetComicsTotalUseCase @Inject constructor(private val repository: ComicsRepository) {

    suspend operator fun invoke(): Int {
        return repository.comicsTotal()
    }
}