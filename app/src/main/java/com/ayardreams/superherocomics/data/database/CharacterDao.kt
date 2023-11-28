package com.ayardreams.superherocomics.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {

    @Query("SELECT * FROM MarvelCharacter ORDER BY name ASC")
    fun getAll(): Flow<List<MarvelCharacter>>

    @Query("SELECT * FROM MarvelCharacter WHERE id = :id")
    fun findById(id: Int): Flow<MarvelCharacter>

    @Query("SELECT COUNT(id) FROM MarvelCharacter")
    suspend fun marvelCharactersCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMarvelCharacters(marvelCharacters: List<MarvelCharacter>)
}
