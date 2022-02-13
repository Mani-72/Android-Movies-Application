package com.example.moviesandroidapplication.repository

import com.example.moviesandroidapplication.database.MovieDao
import com.example.moviesandroidapplication.entities.MoviesTable

class LocalRepository(private val movieDao: MovieDao) {

    suspend fun insertMovies(movies: MoviesTable) = movieDao.insertMovies(movies)

    fun getMovieById(id: Double) = movieDao.getMovieById(id)

    fun getAllMovies() = movieDao.getAllMovies()


}