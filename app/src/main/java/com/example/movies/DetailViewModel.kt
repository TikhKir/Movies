package com.example.movies

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.arch.core.util.Function
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.movies.data.FavouriteMovie
import com.example.movies.data.Movie
import com.example.movies.data.MoviesDatabase
import com.example.movies.utils.JSONUtils
import com.example.movies.utils.NetworkUtils
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    private val database = MoviesDatabase.getInstance(getApplication())
    private lateinit var movieLiveData: LiveData<Movie>
    private lateinit var isFavourite: LiveData<Int>
    private val compositeDisposable = CompositeDisposable()


    fun getAnyMovieById(movieId: Int, fromFavouriteActivity: Boolean): LiveData<Movie> {
        if (fromFavouriteActivity) {
            val favouriteMovie = database.moviesDao().getFavouriteMovieById(movieId)
            movieLiveData = Transformations.map(favouriteMovie, Function<FavouriteMovie,Movie>{
                return@Function FavouriteMovie.convertToNotFavourite(it)
            })
        } else {
            movieLiveData = database.moviesDao().getMovieById(movieId)
        }
        return movieLiveData
    }


    fun checkIsFavourite(id: Int): LiveData<Int> {
        isFavourite = database.moviesDao().checkIsFavourite(id)
        return isFavourite
    }

    fun addMovieToFavourite(movie: Movie) {
        Completable.fromAction {
            database.moviesDao().insertFavouriteMovie(FavouriteMovie(movie))
        }.subscribeOn(Schedulers.io())
            .subscribe(object : CompletableObserver {
                override fun onComplete() {
                    Log.e("FAVOURITE_ADD", "ADD COMPLETE!")
                }

                override fun onSubscribe(d: Disposable) {
                    Log.e("FAVOURITE_ADD", "ON SUBSCRIBE!")
                }

                override fun onError(e: Throwable) {
                    Log.e("FAVOURITE_ADD", e.message)
                }

            })
    }

    fun deleteFromFavourite(movie: Movie) {
        Completable.fromAction {
            database.moviesDao().deleteFavouriteMovie(FavouriteMovie(movie))
        }.subscribeOn(Schedulers.io())
            .subscribe(object : CompletableObserver {
                override fun onComplete() {
                    Log.e("FAVOURITE_DELETE", "ADD COMPLETE!")
                }

                override fun onSubscribe(d: Disposable) {
                    Log.e("FAVOURITE_DELETE", "ON SUBSCRIBE!")
                }

                override fun onError(e: Throwable) {
                    Log.e("FAVOURITE_DELETE", e.message)
                }

            })
    }

    fun loadMovieInfo(movieId: Int, fromFavouriteActivity: Boolean) {
        val disposable = NetworkUtils.getMovieByID(movieId)
            .map { JSONUtils.getMovieDataFromJsonObject(it) }
            .subscribeOn(Schedulers.io())
            .subscribe({
                if (fromFavouriteActivity) {
                    it?.let { database.moviesDao().insertFavouriteMovie(FavouriteMovie(it)) }
                } else
                    it?.let { database.moviesDao().insertMovie(it) }
            }, {
                Log.e("LOAD_DETAIL_ERROR", it.message)
            })
        compositeDisposable.add(disposable)
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}