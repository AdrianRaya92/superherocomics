package com.ayardreams.superherocomics.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MarvelCharacter::class], version = 1, exportSchema = false)
abstract class CharacterDatabase : RoomDatabase() {

    abstract fun characterDao(): CharacterDao
}