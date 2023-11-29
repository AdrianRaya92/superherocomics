package com.ayardreams.superherocomics.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ComicsDao {

    @Query("SELECT * FROM MarvelComics ORDER BY title ASC")
    fun getAll(): Flow<List<MarvelComics>>

    @Query("SELECT * FROM MarvelComics WHERE id = :id")
    fun findById(id: Int): Flow<MarvelComics>

    @Query("SELECT currentDate FROM MarvelComics LIMIT 1")
    suspend fun getFirstCurrentDate(): String?

    @Query("SELECT COUNT(id) FROM MarvelComics")
    suspend fun marvelComicsCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMarvelComics(marvelComics: List<MarvelComics>)

    @Query("DELETE FROM MarvelComics")
    suspend fun deleteAll()
}
