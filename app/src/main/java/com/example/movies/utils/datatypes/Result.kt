package com.example.movies.utils.datatypes

import com.example.movies.repository.model.Movie

data class Result<out T>(
    var resultType: ResultType,
    val data: T? = null,
    val error: Exception? = null
) {

    companion object {
        fun <T> networkSuccess(data: T?): Result<T> {
            return Result(ResultType.FROM_NW, data)
        }

        fun <T> databaseSuccess(data: T?): Result<T> {
            return Result(ResultType.FROM_DB, data)
        }

        fun <T> error(error: Exception? = null): Result<T> {
            return Result(ResultType.ERROR, error = error)
        }

        fun movieFavourite(movie: Movie?): Result<Movie> {
            return Result(ResultType.MOVIE_FAV, movie)
        }

        fun movieNotFavourite(movie: Movie?): Result<Movie> {
            return Result(ResultType.MOVIE_NOT_FAV, movie)
        }
    }
}