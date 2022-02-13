package com.example.moviesandroidapplication.services

import com.example.moviesandroidapplication.utils.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("api_key") apiKey: String = API_KEY, @Query("language") language: String = "en-US", @Query("page") page: Int) : Response<Map<String, Any>>

    @GET("movie/upcoming")
    suspend fun getUpComingMovies(@Query("api_key") apiKey: String = API_KEY, @Query("language") language: String = "en-US" , @Query("page") page: Int) : Response<Map<String, Any>>

    @GET("search/movie")
    suspend fun getSearchedMovies(@Query("api_key") apiKey: String = API_KEY, @Query("language") language: String = "en-US" , @Query("query") search: String , @Query("page") page: Int) : Response<Map<String, Any>>
}