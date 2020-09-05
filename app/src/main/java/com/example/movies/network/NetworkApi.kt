package com.example.movies.network

import com.example.movies.constants.SortTypes
import com.example.movies.repository.model.Movie
import com.example.movies.repository.model.Review
import com.example.movies.repository.model.Trailer
import com.example.movies.utils.datatypes.Result
import io.reactivex.Single

interface NetworkApi {

    fun getMovies(sortMethod: SortTypes, page: Int): Single<Result<List<Movie>>>

    fun getMovieByID(movieId: Int): Single<Result<Movie>>

    fun getTrailerById(movieId: Int): Single<List<Trailer>>

    fun getReviewById(movieId: Int): Single<List<Review>>
}