package com.ayardreams.usecase

import com.ayardreams.data.CharactersRepository
import javax.inject.Inject

class GetCharactersUseCase @Inject constructor(private val repository: CharactersRepository) {

    operator fun invoke() = repository.charactersMarvel
}