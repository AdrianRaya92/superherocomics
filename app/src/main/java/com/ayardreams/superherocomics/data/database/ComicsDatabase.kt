package com.ayardreams.superherocomics.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MarvelComics::class], version = 1, exportSchema = false)
abstract class ComicsDatabase : RoomDatabase() {

    abstract fun comicsDao(): ComicsDao
}