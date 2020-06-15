package com.example.movies

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movies.data.Movie
import com.example.movies.data.MoviesDatabase
import com.example.movies.data.Review
import com.example.movies.data.Trailer
import com.example.movies.utils.JSONUtils
import com.example.movies.utils.NetworkUtils
import com.example.movies.utils.rxutils.BaseViewModel
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class DetailViewModel(application: Application) : BaseViewModel(application) {
    private val database = MoviesDatabase.getInstance(getApplication())


    fun getMovieById(movieId: Int): Observable<Movie?> {
        return NetworkUtils.getMovieByID(movieId)
            .map { JSONUtils.getMovieDataFromJsonObject(it) }
            .subscribeOn(Schedulers.io())
            .doOnSuccess {
                it?.let { database.moviesDao().newUpsertMovie(
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
                    0
                )}
            }
            .toObservable()
            .onErrorResumeNext(database.moviesDao().getMovieById(movieId))

    }

    fun getReviews(movieId: Int): Single<List<Review>> {
        return NetworkUtils.getJSONForReviews(movieId)
            .map { JSONUtils.getListReviewsDataFromJsonObject(it) }
            .subscribeOn(Schedulers.io())
    }

    fun getTrailers(movieId: Int): Single<List<Trailer>> {
        return NetworkUtils.getJSONForVideos(movieId)
            .map { JSONUtils.getListTrailersDataFromJsonObject(it) }
            .subscribeOn(Schedulers.io())
    }


    fun addMovieToFavourite(movie: Movie) {
        Completable.fromAction {
            database.moviesDao().setAsFavourite(movie.id)
        }.subscribeOn(Schedulers.io())
            .subscribe(object : CompletableObserver {
                override fun onComplete() {
                    Log.e("FAVOURITE_SET", "SET COMPLETE!")
                }

                override fun onSubscribe(d: Disposable) {
                    Log.e("FAVOURITE_SET", "ON SUBSCRIBE!")
                }

                override fun onError(e: Throwable) {
                    Log.e("FAVOURITE_SET", e.message)
                }

            })
    }

    fun deleteFromFavourite(movie: Movie) {
        Completable.fromAction {
            database.moviesDao().setAsNotFavourite(movie.id)
        }.subscribeOn(Schedulers.io())
            .subscribe(object : CompletableObserver {
                override fun onComplete() {
                    Log.e("FAVOURITE_UNSET", "UNSET COMPLETE!")
                }

                override fun onSubscribe(d: Disposable) {
                    Log.e("FAVOURITE_UNSET", "ON SUBSCRIBE!")
                }

                override fun onError(e: Throwable) {
                    Log.e("FAVOURITE_UNSET", e.message)
                }

            })
    }




}