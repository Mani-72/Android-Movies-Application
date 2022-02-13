package com.example.moviesandroidapplication.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviesandroidapplication.entities.MoviesTable
import com.example.moviesandroidapplication.model.Movies
import com.example.moviesandroidapplication.repository.LocalRepository
import com.example.moviesandroidapplication.repository.ServerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val serverRepository: ServerRepository,
    private val localRepository: LocalRepository
) : ViewModel() {


    val moviesLiveData = MutableLiveData<List<Movies>>()
    val errorMessage = MutableLiveData<String>()
    var job: Job? = null

    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    val loading = MutableLiveData<Boolean>()

    val likedMoviesLiveData: LiveData<List<MoviesTable>> = localRepository.getAllMovies()

    fun getPopularMovies() {
        try {
            job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                val response = serverRepository.getPopularMovies()
                getMovies(response)
            }
        } catch (e: Exception) {
            Log.e("Error", "getPopularMovies Error in ViewModel: ${e.message}")
        }
    }

    fun getUpcomingMovies() {
        try {
            job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                val response = serverRepository.getUpcomingMovies()
                getMovies(response)
            }
        } catch (e: Exception) {
            Log.e("Error", "getUpcomingMovies Error in ViewModel: ${e.message}")
        }
    }

    fun getSearchedMovies(search: String) {
        try {
            job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                val response = serverRepository.getSearchedMovies(search)
                getMovies(response)
            }
        } catch (e: Exception) {
            Log.e("Error", "getSearchedMovies Error in ViewModel: ${e.message}")
        }
    }


    private suspend fun getMovies(response: Response<Map<String, Any>>) {
        withContext(Dispatchers.Main) {
            if (response.isSuccessful) {
                loading.value = false
                val resultMap: Map<String, Any> = response.body() as Map<String, Any>

                var result: MutableList<Movies> = mutableListOf<Movies>()
                resultMap["results"].apply {
                    val temp = this as ArrayList<Any>
                    temp.forEach {
                        val temp = Movies.fromjson(it as Map<String, Any>)
                        result.add(temp)
                    }
                    moviesLiveData.postValue(result)
                }
            } else {
                onError("Error : ${response.message()} ")
            }
        }
    }

    private fun onError(message: String) {
        errorMessage.value = message
        loading.value = false
    }


    fun saveLikedMovies(it: Movies) {
        CoroutineScope(Dispatchers.Main).launch {
            localRepository.insertMovies(
                MoviesTable(
                    adult = it.adult,
                    backdrop_path = it.backdrop_path,
                    original_language = it.original_language,
                    overview = it.overview,
                    poster_path = it.poster_path,
                    release_date = it.release_date,
                    title = it.title,
                    id = it.id,
                )
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

}