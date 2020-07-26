package com.example.movies.network

import com.example.movies.constants.SortTypes
import com.example.movies.data.model.Movie
import com.example.movies.data.model.Review
import com.example.movies.data.model.Trailer
import io.reactivex.Single

interface NetworkApi {

    fun getMovies(sortMethod: SortTypes, page: Int): Single<List<Movie>>

    fun getMovieByID(movieId: Int): Single<Movie>

    fun getTrailerById(movieId: Int): Single<Trailer>

    fun getReviewById(movieId: Int): Single<Review>
}