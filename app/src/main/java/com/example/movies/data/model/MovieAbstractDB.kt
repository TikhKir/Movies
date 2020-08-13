package com.example.movies.data.model

abstract class MovieAbstractDB {

    abstract val id: Int
    abstract val voteCount: Int?
    abstract val title: String
    abstract val originalTitle: String
    abstract val overview: String
    abstract val posterPath: String
    abstract val bigPosterPath: String
    abstract val backdropPath: String
    abstract val voteAverage: Double?
    abstract val releaseDate: String

    //for saving order of movies in recyclers
    abstract var localId: Int?
}