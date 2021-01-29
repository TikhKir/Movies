package com.example.movies.network.services

import com.example.movies.BuildConfig
import com.example.movies.network.rawmodel.MovieRaw
import com.example.movies.network.rawmodel.MovieResponse
import com.example.movies.network.rawmodel.ReviewResponse
import com.example.movies.network.rawmodel.TrailerResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BaseService {

    companion object {
        private const val API_KEY = BuildConfig.API_KEY_TMDB

        private const val PARAMS_API_KEY = "api_key"
        private const val PARAMS_LANGUAGE = "language"
        private const val PARAMS_SORT_BY = "sort_by"
        private const val PARAMS_PAGE = "page"
        private const val PARAMS_MIN_VOTE_COUNT = "vote_count.gte"

        private const val LANGUAGE_VALUE = "ru-RU"
        private const val MIN_VOTE_COUNT_VALUE = "1000"

        const val KEY_YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v="

        const val BASE_POSTER_URL = "https://image.tmdb.org/t/p/"
        const val SMALL_POSTER_SIZE = "w200"
        const val BIG_POSTER_SIZE = "w500"
    }

    @GET("discover/movie")
    fun getMovies(
        @Query(PARAMS_SORT_BY) sortBy: String,
        @Query(PARAMS_PAGE) page: Int,
        @Query(PARAMS_LANGUAGE) language: String = LANGUAGE_VALUE,
        @Query(PARAMS_MIN_VOTE_COUNT) minVoteValue: String = MIN_VOTE_COUNT_VALUE,
        @Query(PARAMS_API_KEY) apiKey: String = API_KEY
    ): Single<MovieResponse>

    @GET("movie/{movie_id}")
    fun getMovieByID(
        @Path("movie_id") movieId: Int,
        @Query(PARAMS_LANGUAGE) language: String = LANGUAGE_VALUE,
        @Query(PARAMS_API_KEY) apiKey: String = API_KEY
    ): Single<MovieRaw>

    @GET("movie/{movie_id}/videos")
    fun getTrailerById(
        @Path("movie_id") movieId: Int,
        @Query(PARAMS_LANGUAGE) language: String = LANGUAGE_VALUE,
        @Query(PARAMS_API_KEY) apiKey: String = API_KEY
    ): Single<TrailerResponse>

    @GET("movie/{movie_id}/reviews")
    fun getReviewById(
        @Path("movie_id") movieId: Int,
        //@Query(PARAMS_LANGUAGE) language: String = LANGUAGE_VALUE,
        @Query(PARAMS_API_KEY) apiKey: String = API_KEY
    ): Single<ReviewResponse>

    @GET("search/movie")
    fun searchMovie(
        @Query("query") query: String,
        @Query("primary_release_year") year: Int?,
        @Query("include_adult") adult: Boolean,
        @Query(PARAMS_LANGUAGE) language: String = LANGUAGE_VALUE,
        @Query(PARAMS_API_KEY) apiKey: String = API_KEY
    ): Single<MovieResponse>
}