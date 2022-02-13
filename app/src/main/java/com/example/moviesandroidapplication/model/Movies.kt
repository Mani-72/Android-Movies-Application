package com.example.moviesandroidapplication.model

import com.example.moviesandroidapplication.utils.Constants.BASE_IMAGE_API_URL

data class Movies (
    val id: Double,
    val adult: Boolean?,
    val backdrop_path: String?,
    val original_language: String?,
    val overview: String?,
    val poster_path: String?,
    val release_date: String?,
    val title: String?,
        ) {
    companion object {
        fun fromjson(jsonData: Map<String, Any>): Movies {
            return Movies(
                adult = jsonData["adult"] as Boolean?,
                backdrop_path = jsonData["backdrop_path"] as String?,
                original_language =jsonData["original_language"] as String?,
                overview = jsonData["overview"] as String?,
                poster_path = jsonData["poster_path"] as String?,
                release_date = jsonData["release_date"] as String?,
                title = jsonData["title"] as String?,
                id = jsonData["id"] as Double
            )
        }
    }

    fun posterURL(): String {
        return BASE_IMAGE_API_URL+poster_path
    }



}