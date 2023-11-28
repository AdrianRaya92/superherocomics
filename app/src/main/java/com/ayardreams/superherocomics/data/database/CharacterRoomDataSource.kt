package com.ayardreams.superherocomics.data.database

import com.ayardreams.data.datasource.CharacterLocalDataSource
import com.ayardreams.domain.MarvelCharacter
import com.ayardreams.domain.Error
import com.ayardreams.superherocomics.data.tryCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.ayardreams.superherocomics.data.database.MarvelCharacter as DbCharacter

class CharacterRoomDataSource @Inject constructor(private val characterDao: CharacterDao) :
    CharacterLocalDataSource {

    override val characters: Flow<List<MarvelCharacter>> = characterDao.getAll().map { it.toDomainModel() }

    override suspend fun isEmpty(): Boolean = characterDao.marvelCharactersCount() == 0

    override fun findById(id: Int): Flow<MarvelCharacter> = characterDao.findById(id).map { it.toDomainModel() }

    override suspend fun save(marvelCharacters: List<MarvelCharacter>): Error? = tryCall {
        characterDao.insertMarvelCharacters(marvelCharacters.fromDomainModel())
    }.fold(
        ifLeft = { it },
        ifRight = { null }
    )
}

private fun List<DbCharacter>.toDomainModel(): List<MarvelCharacter> = map { it.toDomainModel() }

private fun DbCharacter.toDomainModel(): MarvelCharacter =
    MarvelCharacter(
        id,
        name,
        description,
        modified,
        resourceURI,
        thumbnail
    )

private fun List<MarvelCharacter>.fromDomainModel(): List<DbCharacter> = map { it.fromDomainModel() }

private fun MarvelCharacter.fromDomainModel(): DbCharacter = DbCharacter(
    id,
    name,
    description,
    modified,
    resourceURI,
    thumbnail
)
