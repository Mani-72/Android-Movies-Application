package com.example.moviesandroidapplication.repository

import com.example.moviesandroidapplication.services.ApiService

class ServerRepository(private val apiService: ApiService) {
    suspend fun getPopularMovies() = apiService.getPopularMovies(page = 1)
    suspend fun getUpcomingMovies() = apiService.getUpComingMovies(page = 1)
    suspend fun getSearchedMovies(search: String) = apiService.getSearchedMovies(search = search, page = 1)
}