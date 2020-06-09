package com.example.movies

import android.app.Application
import android.util.Log
import androidx.arch.core.util.Function
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import com.example.movies.data.Movie
import com.example.movies.data.MoviesDatabase
import com.example.movies.utils.JSONUtils
import com.example.movies.utils.NetworkUtils
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class PopularityViewModel(application: Application) : AndroidViewModel(application) {

    private val database = MoviesDatabase.getInstance(getApplication())
    private val popularityMoviesLiveData: MutableLiveData<List<Movie>> = MutableLiveData()
    private val topRatedMoviesLiveData: MutableLiveData<List<Movie>> = MutableLiveData()
    private val compositeDisposable = CompositeDisposable()

    init {
        val disposable = database.moviesDao().getMoviesBySearchMethod(NetworkUtils.POPULARITY)
            .subscribeOn(Schedulers.io())
            .subscribe({
                popularityMoviesLiveData.postValue(it)
            },{
                Log.e("GET_SINGLE_ERROR", it.message)
            })
        compositeDisposable.add(disposable)

        val disposable2 = database.moviesDao().getMoviesBySearchMethod(NetworkUtils.TOP_RATED)
            .subscribeOn(Schedulers.io())
            .subscribe({
                topRatedMoviesLiveData.postValue(it)
            },{
                Log.e("GET_SINGLE_ERROR", it.message)
            })
        compositeDisposable.add(disposable2)
    }

    fun getPopularityMovieList(): LiveData<List<Movie>> {
        return popularityMoviesLiveData
    }

    fun getTopRatedMovieList(): LiveData<List<Movie>> {
        return topRatedMoviesLiveData
    }


    fun loadData(methodOfSort: Int) {
        val disposable = NetworkUtils.getMovies(methodOfSort, 1)
            .map { JSONUtils.getListMovieDataFromJsonObject(it) }
            .subscribeOn(Schedulers.single())
            .subscribe({
                when (methodOfSort) {
                    NetworkUtils.POPULARITY -> popularityMoviesLiveData.postValue(it)
                    NetworkUtils.TOP_RATED -> topRatedMoviesLiveData.postValue(it)
                }

                it.map {
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
                        methodOfSort
                    )
                }
            },
                {
                    Log.e("LOAD_ERROR", it.message)
                })
        compositeDisposable.add(disposable)
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

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}