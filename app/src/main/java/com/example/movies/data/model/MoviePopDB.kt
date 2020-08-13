package com.example.movies.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies_popularity")
class MoviePopDB(
    override val id: Int,
    override val voteCount: Int? = null,
    override val title: String = "",
    override val originalTitle: String = "",
    override val overview: String = "",
    override val posterPath: String = "",
    override val bigPosterPath: String = "",
    override val backdropPath: String = "",
    override val voteAverage: Double? = null,
    override val releaseDate: String = ""

) : MovieAbstractDB() {
    @PrimaryKey
    override var localId: Int? = null
}