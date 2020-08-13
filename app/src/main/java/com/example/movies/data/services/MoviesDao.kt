package com.example.movies.data.services

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import com.example.movies.data.model.Movie
import com.example.movies.utils.datatypes.Result
import io.reactivex.Maybe
import io.reactivex.Observable

@Dao
interface MoviesDao {

    @Query("SELECT * FROM movies")
    fun getAllMovies(): LiveData<List<Movie>>

    //см изумительный костыль ниже
    @Query("SELECT * FROM movies WHERE (searchBy = :method) OR (searchBy = 2)")
    fun getMoviesBySearchMethod(method: Int): Maybe<List<Movie>>

    @Query("SELECT * FROM movies WHERE id == :movieId")
    fun getMovieById(movieId: Int): Observable<Movie>

    @Query("DELETE FROM movies WHERE isFavourite = 0")
    fun deleteAllMovies()

    @Delete
    fun deleteMovie(movie: Movie)




    @Query("SELECT 1 FROM movies WHERE id = :id LIMIT 1")
    fun checkInsert(id: Int): Int

    @Query("INSERT INTO movies(id, voteCount, title, originalTitle, overview, posterPath, bigPosterPath, backdropPath, voteAverage, releaseDate, isFavourite, searchBy, identifier) VALUES(:id, :voteCount, :title, :originalTitle, :overview, :posterPath, :bigPosterPath, :backdropPath, :voteAverage, :releaseDate, :isFavourite, :searchBy, :identifier)")
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
        searchBy: Int,
        identifier: Int = id
    )

    //изумительный костыль с IFNULL в качестве затычки для проблемы с перезаписывание searchBy при приходе одинаковых фильмов в оба списка
    @Query("UPDATE movies SET id=:id, voteCount=:voteCount, title=:title, originalTitle=:originalTitle, overview=:overview, posterPath=:posterPath, bigPosterPath=:bigPosterPath, backdropPath=:backdropPath, voteAverage=:voteAverage, releaseDate=:releaseDate, identifier=:identifier, searchBy=IFNULL((SELECT 2 FROM movies WHERE id = :id AND searchBy<2 AND searchBy!=:searchBy), searchBy)  WHERE id = :id")
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
        releaseDate: String,
        identifier: Int = id,
        searchBy: Int
    )


    fun upsertMovie(
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
            updateMovie(id, voteCount, title, originalTitle, overview, posterPath, bigPosterPath, backdropPath, voteAverage, releaseDate, searchBy = searchBy)
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