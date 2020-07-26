package com.example.movies.data

import com.example.movies.constants.SortTypes
import com.example.movies.data.model.Movie
import io.reactivex.Maybe
import io.reactivex.Observable

interface DataApi {
    fun getMoviesBySearchMethod(sortMethod: SortTypes): Maybe<List<Movie>>

    fun getMovieById(movieId: Int): Observable<Movie>

    fun deleteAllMovies()

    fun upsertMovie(movie: Movie, sortMethod: SortTypes)
}