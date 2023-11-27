package com.ayardreams.usecase

import com.ayardreams.data.CharactersRepository
import com.ayardreams.domain.Error
import javax.inject.Inject

class RequestMarvelCharactersUseCase @Inject constructor(private val repository: CharactersRepository) {

    suspend operator fun invoke(offset: Int): Error? {
        return repository.requestMarvelCharacters(offset)
    }
}