package com.example.movies.data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_movie")
data class FavouriteMovie(
    @PrimaryKey
    val id: Int,
    val voteCount: Int? = null,
    val title: String = "",
    val originalTitle: String = "",
    val overview: String = "",
    val posterPath: String = "",
    val bigPosterPath: String = "",
    val backdropPath: String = "",
    val voteAverage: Double? = null,
    val releaseDate: String = ""
) {
    @Ignore
    constructor(movie: Movie) : this(
        id = movie.id,
        voteCount = movie.voteCount,
        title = movie.title,
        originalTitle = movie.originalTitle,
        overview = movie.overview,
        posterPath = movie.posterPath,
        bigPosterPath = movie.bigPosterPath,
        backdropPath = movie.backdropPath,
        voteAverage = movie.voteAverage,
        releaseDate = movie.releaseDate
    )

    companion object {

        fun convertToNotFavourite(favouriteMovie: FavouriteMovie): Movie {
            val id = favouriteMovie.id
            val voteCount = favouriteMovie.voteCount
            val title = favouriteMovie.title
            val originalTitle = favouriteMovie.originalTitle
            val overview = favouriteMovie.overview
            val posterPath = favouriteMovie.posterPath
            val bigPosterPath = favouriteMovie.bigPosterPath
            val backdropPath = favouriteMovie.backdropPath
            val voteAverage = favouriteMovie.voteAverage
            val releaseDate = favouriteMovie.releaseDate

            return Movie(id, voteCount, title, originalTitle, overview, posterPath, bigPosterPath, backdropPath, voteAverage, releaseDate)
        }
    }




}