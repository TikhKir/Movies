package com.example.movies.network

import com.example.movies.constants.SortTypes
import com.example.movies.network.services.BaseService
import com.example.movies.repository.model.Movie
import com.example.movies.repository.model.Review
import com.example.movies.repository.model.Trailer
import com.example.movies.utils.datatypes.Result
import io.reactivex.Single
import javax.inject.Inject

class NetworkApiImpl @Inject constructor(
    private val baseService: BaseService
) : NetworkApi {

    override fun getMovies(sortMethod: SortTypes, page: Int): Single<Result<List<Movie>>> {
        return baseService.getMovies(sortMethod.value, page)
            .map { it.movieListRaw.map { it.toMovie() }}
            .map { Result.networkSuccess(it) }
    }

    override fun getMovieByID(movieId: Int): Single<Result<Movie>> {
        return baseService.getMovieByID(movieId)
            .map { it.toMovie() }
            .map { Result.networkSuccess(it) }
    }

    override fun searchMovie(query: String, year: Int?, adult: Boolean): Single<List<Movie>> {
        return baseService.searchMovie(query, year, adult)
            .map { it.movieListRaw.map { it.toMovie() } }
    }

    override fun getTrailerById(movieId: Int): Single<List<Trailer>> {
        return baseService.getTrailerById(movieId)
            .map { it.trailerListRaw.map { it.toTrailer() } }
    }

    override fun getReviewById(movieId: Int): Single<List<Review>> {
        return baseService.getReviewById(movieId)
            .map { it.reviewListRaw.map { it.toReview() }}
    }
}