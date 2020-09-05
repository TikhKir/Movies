package com.example.movies.data

import com.example.movies.constants.SortTypes
import com.example.movies.repository.model.Movie
import com.example.movies.utils.datatypes.Result
import io.reactivex.Maybe
import io.reactivex.Observable

interface DataApi {
    fun getMoviesBySearchMethod(sortMethod: SortTypes): Maybe<Result<List<Movie>>>

    fun getMovieById(movieId: Int): Maybe<Result<Movie>>

    fun deleteAllMovies()

    fun upsertMovie(movie: Movie, sortMethod: SortTypes)

    fun addMovieToFavourite(movieId: Int)

    fun deleteMovieFromFavourite(movieId: Int)

    fun getFavouriteMovies(): Observable<List<Movie>>
}