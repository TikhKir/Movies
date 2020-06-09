package com.example.movies.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class Movie(
    val id: Int,
    val voteCount: Int? = null,
    val title: String = "",
    val originalTitle: String = "",
    val overview: String = "",
    val posterPath: String = "",
    val bigPosterPath: String = "",
    val backdropPath: String = "",
    val voteAverage: Double? = null,
    val releaseDate: String = "",

    val isFavourite: Int = 0,
    val searchBy: Int = 0
) {
    @PrimaryKey(autoGenerate = true)
    var localId: Int? = null
}