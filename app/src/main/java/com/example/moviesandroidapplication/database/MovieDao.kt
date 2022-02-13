package com.example.moviesandroidapplication.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.moviesandroidapplication.entities.MoviesTable

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: MoviesTable)

    @Query("select count(*) from movie_table where id = :id" )
    fun getMovieById(id: Double) : LiveData<Int>

    @Query("select * from movie_table order by id" )
    fun getAllMovies() : LiveData<List<MoviesTable>>

}