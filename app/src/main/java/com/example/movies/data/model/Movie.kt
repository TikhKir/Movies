package com.example.movies.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.movies.utils.diffutil.Identified

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
    val searchBy: Int = 0,

    override val identifier: Int = id
): Identified {
    @PrimaryKey(autoGenerate = true)
    var localId: Int? = null
}