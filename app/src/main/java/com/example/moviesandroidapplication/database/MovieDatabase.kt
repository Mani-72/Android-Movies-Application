package com.example.moviesandroidapplication.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.moviesandroidapplication.entities.MoviesTable

@Database(
    entities = [MoviesTable::class],
    version = 1
)

abstract class MovieDatabase: RoomDatabase() {
    abstract fun getMovieDao(): MovieDao
}