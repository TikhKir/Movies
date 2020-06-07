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
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import java.net.SocketException

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    private val database = MoviesDatabase.getInstance(getApplication())
    private lateinit var movieLiveData: LiveData<Movie>
    private val compositeDisposable = CompositeDisposable()
    private var reviewsLiveData: MutableLiveData<List<Review>> = MutableLiveData()
    private var trailersLiveData: MutableLiveData<List<Trailer>> = MutableLiveData()

    fun getAnyMovieById(movieId: Int): LiveData<Movie> {
            movieLiveData = database.moviesDao().getMovieById(movieId)
        return movieLiveData
    }

    fun getReviews(): LiveData<List<Review>> {
        return reviewsLiveData
    }

    fun getTrailers(): LiveData<List<Trailer>> {
        return trailersLiveData
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

    fun loadMovieInfo(movieId: Int) {
        val disposable = NetworkUtils.getMovieByID(movieId)
            .map { JSONUtils.getMovieDataFromJsonObject(it) }
            .subscribeOn(Schedulers.io())
            .subscribe({
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
                    ) }
            }, {
                Log.e("LOAD_DETAIL_ERROR", it.message)
            })
        compositeDisposable.add(disposable)
    }

    fun loadReviewsData(movieId: Int) {
        val disposable = NetworkUtils.getJSONForReviews(movieId)
            .map { JSONUtils.getListReviewsDataFromJsonObject(it) }
            .subscribeOn(Schedulers.io())
            .subscribe({
                reviewsLiveData.postValue(it)
            },{
                Log.e("LOAD_REVIEWS_ERROR", it.message)
            })
        compositeDisposable.add(disposable)
    }

    fun loadTrailersData(movieId: Int) {
        val disposable = NetworkUtils.getJSONForVideos(movieId)
            .map { JSONUtils.getListTrailersDataFromJsonObject(it) }
            .subscribeOn(Schedulers.io())
            .subscribe({
                trailersLiveData.postValue(it)
            },{
                Log.e("LOAD_TRAILERS_ERROR", it.message)
            })
        compositeDisposable.add(disposable)
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}