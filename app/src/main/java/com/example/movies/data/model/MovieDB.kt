package com.example.movies.data.model

import com.example.movies.repository.model.Movie

abstract class MovieDB {
    abstract val id: Int
    abstract val voteCount: Int?
    abstract val title: String
    abstract val originalTitle: String
    abstract val overview: String
    abstract val posterPath: String
    abstract val bigPosterPath: String
    abstract val backdropPath: String
    abstract val voteAverage: Double?
    abstract val releaseDate: String
    abstract var localId: Int?

    fun toMovie(): Movie {
        return Movie(
            id,
            voteCount,
            title,
            originalTitle,
            overview,
            posterPath,
            bigPosterPath,
            backdropPath,
            voteAverage,
            releaseDate,
            localId
        )
    }

}