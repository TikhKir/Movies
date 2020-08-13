package com.example.movies.data

import android.app.Application
import android.util.Log
import com.example.movies.constants.SortTypes
import com.example.movies.data.model.Movie
import com.example.movies.utils.datatypes.Result
import com.example.movies.utils.datatypes.ResultType
import io.reactivex.Maybe
import io.reactivex.MaybeSource
import io.reactivex.Observable

class DataApiImpl(val application: Application) : DataApi {

    private val database = MoviesDatabase.getInstance(application)

    override fun getMoviesBySearchMethod(sortMethod: SortTypes): Maybe<Result<List<Movie>>> {
        return database.moviesDao().getMoviesBySearchMethod(sortMethod.ordinal)
            .doOnSuccess {
                var temp = 0
                it.map {
                    temp++
                }
                Log.e("GETDB", "$temp ${sortMethod.name}")
            }
            .map { Result.databaseSuccess(it) }
    }

    override fun getMovieById(movieId: Int): Observable<Movie> {
        return database.moviesDao().getMovieById(movieId)
    }

    override fun deleteAllMovies() {
        database.moviesDao().deleteAllMovies()
    }

    override fun upsertMovie(movie: Movie, sortMethod: SortTypes) {
        database.moviesDao().upsertMovie(
            movie.id,
            movie.voteCount,
            movie.title,
            movie.originalTitle,
            movie.overview,
            movie.posterPath,
            movie.bigPosterPath,
            movie.backdropPath,
            movie.voteAverage,
            movie.releaseDate,
            movie.isFavourite,
            sortMethod.ordinal
        )
    }

}