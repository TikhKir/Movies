package com.example.movies.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MoviesDao {

    @Query("SELECT * FROM movies")
    fun getAllMovies(): LiveData<List<Movie>>

    @Query("SELECT * FROM movies WHERE id == :movieId")
    fun getMovieById(movieId: Int): LiveData<Movie>

    @Query("DELETE FROM movies")
    fun deleteAllMovies()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: Movie)

    @Delete
    fun deleteMovie(movie: Movie)





    @Query("SELECT * FROM favourite_movie")
    fun getAllFavouriteMovies(): LiveData<List<FavouriteMovie>>

    @Query("SELECT * FROM favourite_movie WHERE id == :movieId")
    fun getFavouriteMovieById(movieId: Int): LiveData<FavouriteMovie>

    @Delete
    fun deleteFavouriteMovie(favouriteMovie: FavouriteMovie)

    @Query("SELECT 1 FROM favourite_movie WHERE id = :id")
    fun checkIsFavourite(id: Int): LiveData<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavouriteMovie(favouriteMovie: FavouriteMovie)
}