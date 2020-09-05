package com.example.movies.network

import com.example.movies.constants.BaseUrl
import com.example.movies.constants.SortTypes
import com.example.movies.network.services.BaseService
import com.example.movies.repository.model.Movie
import com.example.movies.repository.model.Review
import com.example.movies.repository.model.Trailer
import com.example.movies.utils.datatypes.Result
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class NetworkApiImpl : NetworkApi {

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .baseUrl(BaseUrl.BASE_URL.url)
        .build()

    private val baseService = retrofit.create(BaseService::class.java)

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

    override fun getTrailerById(movieId: Int): Single<List<Trailer>> {
        return baseService.getTrailerById(movieId)
            .map { it.trailerListRaw.map { it.toTrailer() } }
    }

    override fun getReviewById(movieId: Int): Single<List<Review>> {
        return baseService.getReviewById(movieId)
            .map { it.reviewListRaw.map { it.toReview() }}
    }
}