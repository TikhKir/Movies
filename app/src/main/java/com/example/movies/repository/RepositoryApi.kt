package com.example.movies.repository

import com.example.movies.constants.SortTypes
import com.example.movies.data.model.Movie
import com.example.movies.utils.datatypes.Result
import io.reactivex.Observable
import io.reactivex.Single

interface RepositoryApi {

    fun getMoviesPage(sortTypes: SortTypes, page: Int): Observable<Result<List<Movie>>>

    fun getMovieById(movieId: Int): Observable<Movie>
}