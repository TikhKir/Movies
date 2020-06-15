package com.example.movies

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.movies.data.Movie
import com.example.movies.data.MoviesDatabase
import com.example.movies.utils.JSONUtils
import com.example.movies.utils.NetworkUtils
import com.example.movies.utils.rxutils.BaseViewModel
import io.reactivex.*
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class PopularityViewModel(application: Application) : BaseViewModel(application) {

    private val database = MoviesDatabase.getInstance(getApplication())
//    var isLoadingTopRated: Boolean = false
//    var isLoadingPopularity: Boolean = false
    var pageTopRated: Int = 1
    var pagePopularity: Int = 1


    fun getPopularity(): Observable<List<Movie>> {
        return NetworkUtils.getMovies(NetworkUtils.POPULARITY, pagePopularity)
            .subscribeOn(Schedulers.io())
            .map { JSONUtils.getListMovieDataFromJsonObject(it) }
            .doOnSuccess {
                it?.map {
                    database.moviesDao().newUpsertMovie(
                        it.id,
                        it.voteCount,
                        it.title,
                        it.originalTitle,
                        it.overview,
                        it.posterPath,
                        it.bigPosterPath,
                        it.backdropPath,
                        it.voteAverage,
                        it.releaseDate,
                        it.isFavourite,
                        NetworkUtils.POPULARITY
                    )
                }
                Log.e("TAG", "pop insert to DB")
            }
            .toObservable()
            .onErrorResumeNext(
                database.moviesDao().getMoviesBySearchMethod(NetworkUtils.POPULARITY)
            )


    }



fun getTopRated(): Observable<List<Movie>> {
    return NetworkUtils.getMovies(NetworkUtils.TOP_RATED, pageTopRated)
        .subscribeOn(Schedulers.io())
        .map { JSONUtils.getListMovieDataFromJsonObject(it) }
        .doOnSuccess {
            it?.map {
                database.moviesDao().newUpsertMovie(
                    it.id,
                    it.voteCount,
                    it.title,
                    it.originalTitle,
                    it.overview,
                    it.posterPath,
                    it.bigPosterPath,
                    it.backdropPath,
                    it.voteAverage,
                    it.releaseDate,
                    it.isFavourite,
                    NetworkUtils.TOP_RATED
                )
            }
            Log.e("TAG", "top insert to DB")
        }
        .toObservable()
        .onErrorResumeNext(database.moviesDao().getMoviesBySearchMethod(NetworkUtils.TOP_RATED))
}


fun deleteMovies() {
    Completable.fromAction {
        database.moviesDao().deleteAllMovies()
    }.subscribeOn(Schedulers.io())
        .subscribe(object : CompletableObserver {
            override fun onComplete() {
                Log.e("DELETE_ALL", "DELETE COMPLETE!")
            }

            override fun onSubscribe(d: Disposable) {
                Log.e("DELETE_ALL", "ON SUBSCRIBE!")
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

        })
}

fun deleteOneMovie(movie: Movie) {
    Completable.fromAction {
        database.moviesDao().deleteMovie(movie)
    }.subscribeOn(Schedulers.io())
        .subscribe(object : CompletableObserver {
            override fun onComplete() {
                Log.e("DELETE_ONE", "DELETE COMPLETE!")
            }

            override fun onSubscribe(d: Disposable) {
                Log.e("DELETE_ONE", "ON SUBSCRIBE!")
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

        })
}


}