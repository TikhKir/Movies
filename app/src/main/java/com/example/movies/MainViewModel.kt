package com.example.movies

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.movies.data.Movie
import com.example.movies.data.MoviesDatabase
import com.example.movies.utils.JSONUtils
import com.example.movies.utils.NetworkUtils
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = MoviesDatabase.getInstance(getApplication())
    private val moviesLiveData : LiveData<List<Movie>>
    private val compositeDisposable = CompositeDisposable()
    var methodOfSort = NetworkUtils.POPULARITY

    init {
        loadData()
        moviesLiveData = database.moviesDao().getAllMovies()
    }

    fun getMovieList(): LiveData<List<Movie>> {
        return moviesLiveData
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

    fun loadData() {
        val disposable = NetworkUtils.getMovies(methodOfSort, 1)
            .map { JSONUtils.getListMovieDataFromJsonObject(it) }
            .subscribeOn(Schedulers.io())
            .subscribe({
                    it.map {
                        database.moviesDao().insertMovie(it)
                    }
            }, {
                Log.e("LOAD_ERROR", it.message)
            })
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}