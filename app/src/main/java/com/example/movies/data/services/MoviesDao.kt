package com.example.movies.data.services

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.movies.data.model.MovieFavourite
import com.example.movies.data.model.MoviePopDB
import com.example.movies.data.model.MovieTopDB
import io.reactivex.Maybe
import io.reactivex.Observable

@Dao
interface MoviesDao {


    @Query("SELECT * FROM movies_popularity")
    fun getPopularityMovies(): Maybe<List<MoviePopDB>>

    @Query("SELECT * FROM movies_top_rated")
    fun getTopRatedMovies(): Maybe<List<MovieTopDB>>

    @Query("SELECT * FROM movies_favourite")
    fun getFavouriteMovies(): Observable<List<MovieFavourite>>



    @Query("SELECT * FROM movies_top_rated WHERE id == :movieId UNION SELECT * FROM movies_popularity WHERE id == :movieId")
    fun getMovieById(movieId: Int): Maybe<MovieTopDB>

    @Query("SELECT * FROM movies_favourite WHERE id == :movieId")
    fun getFavouriteMovieById(movieId: Int): Maybe<MovieFavourite>



    @Query("DELETE FROM movies_top_rated")
    fun deleteTopRatedMovies()

    @Query("DELETE FROM movies_popularity")
    fun deletePopularityMovies()


    @Query("INSERT INTO movies_favourite SELECT * FROM movies_top_rated WHERE id == :movieId UNION SELECT * FROM movies_popularity WHERE id == :movieId")
    fun addMovieToFavourite(movieId: Int)

    @Query("DELETE FROM movies_favourite WHERE id == :movieId")
    fun deleteMovieFromFavourite(movieId: Int)



    @Query("SELECT 1 FROM movies_top_rated WHERE id = :id LIMIT 1")
    fun checkInsertTopRated(id: Int): Int

    @Query("INSERT INTO movies_top_rated(id, voteCount, title, originalTitle, overview, posterPath, bigPosterPath, backdropPath, voteAverage, releaseDate) VALUES(:id, :voteCount, :title, :originalTitle, :overview, :posterPath, :bigPosterPath, :backdropPath, :voteAverage, :releaseDate)")
    fun insertTopRatedMovie(
        id: Int,
        voteCount: Int?,
        title: String,
        originalTitle: String,
        overview: String,
        posterPath: String,
        bigPosterPath: String,
        backdropPath: String,
        voteAverage: Double?,
        releaseDate: String
    )

    @Query("UPDATE movies_top_rated SET id=:id, voteCount=:voteCount, title=:title, originalTitle=:originalTitle, overview=:overview, posterPath=:posterPath, bigPosterPath=:bigPosterPath, backdropPath=:backdropPath, voteAverage=:voteAverage, releaseDate=:releaseDate  WHERE id = :id")
    fun updateTopRatedMovie(
        id: Int,
        voteCount: Int?,
        title: String,
        originalTitle: String,
        overview: String,
        posterPath: String,
        bigPosterPath: String,
        backdropPath: String,
        voteAverage: Double?,
        releaseDate: String
    )

    @Transaction
    fun upsertTopRatedMovie(
        id: Int,
        voteCount: Int?,
        title: String,
        originalTitle: String,
        overview: String,
        posterPath: String,
        bigPosterPath: String,
        backdropPath: String,
        voteAverage: Double?,
        releaseDate: String
    ) {
        val tmp = checkInsertTopRated(id)
        if (tmp == 1) {
            updateTopRatedMovie(
                id,
                voteCount,
                title,
                originalTitle,
                overview,
                posterPath,
                bigPosterPath,
                backdropPath,
                voteAverage,
                releaseDate
            )
        } else {
            insertTopRatedMovie(
                id,
                voteCount,
                title,
                originalTitle,
                overview,
                posterPath,
                bigPosterPath,
                backdropPath,
                voteAverage,
                releaseDate
            )
        }
    }

    @Query("SELECT 1 FROM movies_popularity WHERE id = :id LIMIT 1")
    fun checkInsertPopularity(id: Int): Int

    @Query("INSERT INTO movies_popularity(id, voteCount, title, originalTitle, overview, posterPath, bigPosterPath, backdropPath, voteAverage, releaseDate) VALUES(:id, :voteCount, :title, :originalTitle, :overview, :posterPath, :bigPosterPath, :backdropPath, :voteAverage, :releaseDate)")
    fun insertPopularityMovie(
        id: Int,
        voteCount: Int?,
        title: String,
        originalTitle: String,
        overview: String,
        posterPath: String,
        bigPosterPath: String,
        backdropPath: String,
        voteAverage: Double?,
        releaseDate: String
    )

    @Query("UPDATE movies_popularity SET id=:id, voteCount=:voteCount, title=:title, originalTitle=:originalTitle, overview=:overview, posterPath=:posterPath, bigPosterPath=:bigPosterPath, backdropPath=:backdropPath, voteAverage=:voteAverage, releaseDate=:releaseDate  WHERE id = :id")
    fun updatePopularityMovie(
        id: Int,
        voteCount: Int?,
        title: String,
        originalTitle: String,
        overview: String,
        posterPath: String,
        bigPosterPath: String,
        backdropPath: String,
        voteAverage: Double?,
        releaseDate: String
    )

    @Transaction
    fun upsertPopularityMovie(
        id: Int,
        voteCount: Int?,
        title: String,
        originalTitle: String,
        overview: String,
        posterPath: String,
        bigPosterPath: String,
        backdropPath: String,
        voteAverage: Double?,
        releaseDate: String
    ) {
        val tmp = checkInsertPopularity(id)
        if (tmp == 1) {
            updatePopularityMovie(
                id,
                voteCount,
                title,
                originalTitle,
                overview,
                posterPath,
                bigPosterPath,
                backdropPath,
                voteAverage,
                releaseDate
            )
        } else {
            insertPopularityMovie(
                id,
                voteCount,
                title,
                originalTitle,
                overview,
                posterPath,
                bigPosterPath,
                backdropPath,
                voteAverage,
                releaseDate
            )
        }
    }









//    @Query("SELECT 1 FROM movies WHERE id = :id AND isFavourite = 1")
//    fun checkIsFavourite(id: Int): LiveData<Int>
//
//    @Query("UPDATE movies SET isFavourite = 1 WHERE id = :id")
//    fun setAsFavourite(id: Int)
//
//    @Query("UPDATE movies SET isFavourite = 0 WHERE id = :id")
//    fun setAsNotFavourite(id: Int)
}