package com.example.movies.network.rawmodel

import com.example.movies.network.services.BaseService
import com.example.movies.repository.model.Movie
import com.google.gson.annotations.SerializedName


data class MovieRaw(
    @SerializedName("id")
    val id: Int,

    @SerializedName("vote_count")
    val voteCount: Int? = null,

    @SerializedName("title")
    val title: String = "",

    @SerializedName("original_title")
    val originalTitle: String = "",

    @SerializedName("overview")
    val overview: String = "",

    @SerializedName("poster_path")
    val posterPath: String = "",

    @SerializedName("backdrop_path")
    val backdropPath: String = "",

    @SerializedName("vote_average")
    val voteAverage: Double? = null,

    @SerializedName("release_date")
    val releaseDate: String? = "",

    @SerializedName("popularity")
    val popularity: Float? = null,

    @SerializedName("video")
    val video: Boolean = false,

    @SerializedName("adult")
    val adult: Boolean = false,

    @SerializedName("original_language")
    val originalLanguage: String,

    @SerializedName("genre_ids")
    val genreIds: List<Long>? = null
) {

    fun toMovie(): Movie {
        return Movie(
            id,
            voteCount,
            title,
            originalTitle,
            overview,
            buildPosterPath(posterPath),
            buildBigPosterPath(posterPath),
            backdropPath,
            voteAverage,
            releaseDate
        )
    }

    private fun buildPosterPath(posterPath: String): String {
        return BaseService.BASE_POSTER_URL + BaseService.SMALL_POSTER_SIZE + posterPath
    }

    private fun buildBigPosterPath(posterPath: String): String {
        return BaseService.BASE_POSTER_URL + BaseService.BIG_POSTER_SIZE + posterPath
    }


}



