package com.example.movies.data

import com.example.movies.constants.SortTypes
import com.example.movies.data.model.MovieDB
import com.example.movies.data.model.MovieFavourite
import com.example.movies.data.services.MoviesDao
import com.example.movies.repository.model.Movie
import com.example.movies.utils.datatypes.Result
import io.reactivex.Maybe
import io.reactivex.Observable
import javax.inject.Inject

class DataApiImpl @Inject constructor(
    private val moviesDao: MoviesDao
) : DataApi {


    override fun getMoviesBySearchMethod(sortMethod: SortTypes): Maybe<Result<List<Movie>>> {
        return when (sortMethod) {
            SortTypes.POPULARITY -> moviesDao.getPopularityMovies()
            SortTypes.TOP_RATED -> moviesDao.getTopRatedMovies()
        }.map {
            Result.databaseSuccess(it.map { it.toMovie() })
        }
    }

    override fun getFavouriteMovies(): Observable<List<Movie>> {
        return moviesDao.getFavouriteMovies()
            .map { it.map { it.toMovie() } }
    }

    override fun getMovieById(movieId: Int): Maybe<Result<Movie>> {
        return moviesDao.getFavouriteMovieById(movieId)
            .map { it as MovieDB }
            .switchIfEmpty(moviesDao.getMovieById(movieId))
            .map {
                if (it is MovieFavourite) Result.movieFavourite(it.toMovie())
                else Result.movieNotFavourite(it.toMovie())
            }
    }

    override fun deleteAllMovies() {
        moviesDao.deletePopularityMovies()
        moviesDao.deleteTopRatedMovies()
    }

    override fun upsertMovies(moviesList: List<Movie>, sortMethod: SortTypes) {
        when (sortMethod) {
            SortTypes.TOP_RATED -> upsertTopRatedMovies(moviesList)
            SortTypes.POPULARITY -> upsertPopularityMovies(moviesList)
        }
    }

    override fun addMovieToFavourite(movie: Movie) {
        moviesDao.addMovieToFavourite(movie.toMovieFavourite())
    }

    override fun deleteMovieFromFavourite(movieId: Int) {
        moviesDao.deleteMovieFromFavourite(movieId)
    }

    private fun upsertTopRatedMovies(moviesList: List<Movie>) {
        val transformedList = moviesList.map { it.toMovieTopDB() }
        moviesDao.upsertTopRatedMovies(transformedList)
    }

    private fun upsertPopularityMovies(moviesList: List<Movie>) {
        val transformedList = moviesList.map { it.toMoviePopDB() }
        moviesDao.upsertPopularityMovies(transformedList)
    }

}