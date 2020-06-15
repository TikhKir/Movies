package com.example.movies.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface MoviesDao {

    @Query("SELECT * FROM movies")
    fun getAllMovies(): LiveData<List<Movie>>

    @Query("SELECT * FROM movies WHERE searchBy = :method")
    fun getMoviesBySearchMethod(method: Int): Observable<List<Movie>>

    @Query("SELECT * FROM movies WHERE id == :movieId")
    fun getMovieById(movieId: Int): Observable<Movie>

    @Query("DELETE FROM movies WHERE isFavourite = 0")
    fun deleteAllMovies()

    @Delete
    fun deleteMovie(movie: Movie)





    @Query("SELECT 1 FROM movies WHERE id = :id LIMIT 1")
    fun checkInsert(id: Int): Int

    @Query("INSERT INTO movies(id, voteCount, title, originalTitle, overview, posterPath, bigPosterPath, backdropPath, voteAverage, releaseDate, isFavourite, searchBy) VALUES(:id, :voteCount, :title, :originalTitle, :overview, :posterPath, :bigPosterPath, :backdropPath, :voteAverage, :releaseDate, :isFavourite, :searchBy)")
    fun insertMovie(
        id: Int,
        voteCount: Int?,
        title: String,
        originalTitle: String,
        overview: String,
        posterPath: String,
        bigPosterPath: String,
        backdropPath: String,
        voteAverage: Double?,
        releaseDate: String,
        isFavourite: Int,
        searchBy: Int
    )

    @Query("UPDATE movies SET id=:id, voteCount=:voteCount, title=:title, originalTitle=:originalTitle, overview=:overview, posterPath=:posterPath, bigPosterPath=:bigPosterPath, backdropPath=:backdropPath, voteAverage=:voteAverage, releaseDate=:releaseDate WHERE id = :id")
    fun updateMovie(
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

    fun newUpsertMovie(
        id: Int,
        voteCount: Int?,
        title: String,
        originalTitle: String,
        overview: String,
        posterPath: String,
        bigPosterPath: String,
        backdropPath: String,
        voteAverage: Double?,
        releaseDate: String,
        isFavourite: Int,
        searchBy: Int
    ) {
        val tmp = checkInsert(id)
        if (tmp == 1) {
            updateMovie(id, voteCount, title, originalTitle, overview, posterPath, bigPosterPath, backdropPath, voteAverage, releaseDate)
        } else {
            insertMovie(id, voteCount, title, originalTitle, overview, posterPath, bigPosterPath, backdropPath, voteAverage, releaseDate, isFavourite, searchBy)
        }
    }







    @Query("SELECT * FROM movies WHERE isFavourite = 1")
    fun getAllFavouriteMovies(): Observable<List<Movie>>

    @Query("SELECT 1 FROM movies WHERE id = :id AND isFavourite = 1")
    fun checkIsFavourite(id: Int): LiveData<Int>

    @Query("UPDATE movies SET isFavourite = 1 WHERE id = :id")
    fun setAsFavourite(id: Int)

    @Query("UPDATE movies SET isFavourite = 0 WHERE id = :id")
    fun setAsNotFavourite(id: Int)
}