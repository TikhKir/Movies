package com.example.movies.repository

import com.example.movies.constants.SortTypes
import com.example.movies.repository.model.Movie
import com.example.movies.repository.model.Review
import com.example.movies.repository.model.Trailer
import com.example.movies.utils.datatypes.Result
import io.reactivex.Observable

interface RepositoryApi {

    fun getMoviesPage(sortTypes: SortTypes, page: Int): Observable<Result<List<Movie>>>

    fun getMovieById(movieId: Int): Observable<Result<Movie>>

    fun addMovieToFavourite(movieId: Int)

    fun deleteMovieFromFavourite(movieId: Int)

    fun getReviewsById(movieId: Int): Observable<List<Review>>

    fun getTrailersById(movieId: Int): Observable<List<Trailer>>

    fun getFavouriteMovies(): Observable<List<Movie>>
}