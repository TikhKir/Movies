package com.example.movies.data.services

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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



    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addMovieToFavourite(movie: MovieFavourite)

    @Query("DELETE FROM movies_favourite WHERE id == :movieId")
    fun deleteMovieFromFavourite(movieId: Int)



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertTopRatedMovie(movieTopDB: MovieTopDB)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertPopularityMovie(moviePopDB: MoviePopDB)


}