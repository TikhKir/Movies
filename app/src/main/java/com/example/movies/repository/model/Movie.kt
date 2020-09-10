package com.example.movies.repository.model

import com.example.movies.data.model.MovieDB
import com.example.movies.data.model.MovieFavourite
import com.example.movies.utils.diffutil.Identified

data class Movie(
    val id: Int,
    val voteCount: Int? = null,
    val title: String = "",
    val originalTitle: String = "",
    val overview: String = "",
    val posterPath: String = "",
    val bigPosterPath: String = "",
    val backdropPath: String? = "",
    val voteAverage: Double? = null,
    val releaseDate: String = "",
    val localId: Int? = null,
    override val identifier: Int = id

): Identified
{
    fun toMovieDB(): MovieDB{
       return MovieFavourite(
           id,
           voteCount,
           title,
           originalTitle,
           overview,
           posterPath,
           bigPosterPath,
           backdropPath,
           voteAverage,
           releaseDate
       )
    }
}