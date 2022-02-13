package com.example.moviesandroidapplication.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_table")
data class MoviesTable(
    @PrimaryKey
    val id: Double,
    val adult: Boolean?,
    val backdrop_path: String?,
    val original_language: String?,
    val overview: String?,
    val poster_path: String?,
    val release_date: String?,
    val title: String?,
)