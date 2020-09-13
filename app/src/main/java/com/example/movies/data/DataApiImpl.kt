package com.example.movies.data

import android.app.Application
import com.example.movies.constants.SortTypes
import com.example.movies.data.model.MovieDB
import com.example.movies.data.model.MovieFavourite
import com.example.movies.repository.model.Movie
import com.example.movies.utils.datatypes.Result
import io.reactivex.Maybe
import io.reactivex.Observable

class DataApiImpl(val application: Application) : DataApi {

    private val database = MoviesDatabase.getInstance(application)

    override fun getMoviesBySearchMethod(sortMethod: SortTypes): Maybe<Result<List<Movie>>> {
        return when (sortMethod) {
            SortTypes.POPULARITY -> database.moviesDao().getPopularityMovies()
            SortTypes.TOP_RATED -> database.moviesDao().getTopRatedMovies()
        }.map {
            Result.databaseSuccess(it.map { it.toMovie() })
        }

    }

    override fun getFavouriteMovies(): Observable<List<Movie>> {
        return database.moviesDao().getFavouriteMovies()
            .map { it.map { it.toMovie() } }
    }

    override fun getMovieById(movieId: Int): Maybe<Result<Movie>> {
        return database.moviesDao().getFavouriteMovieById(movieId)
            .map { it as MovieDB }
            .switchIfEmpty(database.moviesDao().getMovieById(movieId))
            .map {
                if (it is MovieFavourite) Result.movieFavourite(it.toMovie())
                 else Result.movieNotFavourite(it.toMovie())
            }
    }

    override fun deleteAllMovies() {
        database.moviesDao().deletePopularityMovies()
        database.moviesDao().deleteTopRatedMovies()
    }

    override fun upsertMovie(movie: Movie, sortMethod: SortTypes) {
        when (sortMethod) {
            SortTypes.TOP_RATED -> upsertTopRatedMovie(movie)
            SortTypes.POPULARITY -> upsertPopularityMovie(movie)
        }
    }

    override fun addMovieToFavourite(movie: Movie) {
        database.moviesDao().addMovieToFavourite(movie.toMovieFavourite())
    }

    override fun deleteMovieFromFavourite(movieId: Int) {
        database.moviesDao().deleteMovieFromFavourite(movieId)
    }

    private fun upsertTopRatedMovie(movie: Movie) {
        database.moviesDao().upsertTopRatedMovie(movie.toMovieTopDB())
    }

    private fun upsertPopularityMovie(movie: Movie) {
        database.moviesDao().upsertPopularityMovie(movie.toMoviePopDB())
    }

}