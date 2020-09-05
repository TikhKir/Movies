package com.example.movies.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movies.repository.RepositoryApiImpl
import com.example.movies.repository.model.Movie
import com.example.movies.repository.model.Review
import com.example.movies.repository.model.Trailer
import com.example.movies.utils.datatypes.ResultType
import com.example.movies.utils.rxutils.BaseViewModel
import com.example.movies.utils.rxutils.RxComposers
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers

class DetailViewModel(application: Application, movieId: Int) : BaseViewModel(application) {

    val liveDataReview: LiveData<List<Review>> by lazy { loadReviews(movieId) }
    val liveDataTrailers: LiveData<List<Trailer>> by lazy { loadTrailers(movieId) }

    private val repository = RepositoryApiImpl(getApplication())
    private var isFavourite = false
    private var movie: Movie? = null


    fun loadMovie(movieId: Int): LiveData<Movie> {
        val liveDataMovie = MutableLiveData<Movie>()
        execute(
            repository.getMovieById(movieId)
                .compose(RxComposers.applyObservableSchedulers())
                .subscribe({
                    when (it.resultType) {
                        ResultType.MOVIE_NOT_FAV -> isFavourite = false
                        ResultType.MOVIE_FAV -> isFavourite = true
                    }
                    liveDataMovie.postValue(it.data)
                    movie = it.data
                }, {
                    Log.e("LOAD MOVIE ERROR", it.message)
                })
        )
        return liveDataMovie
    }

    fun addMovieToFavourite() {
        movie?.let {
            execute(
                Completable.fromAction {
                    repository.addMovieToFavourite(it.id)
                }.subscribeOn(Schedulers.io())
                    .subscribe({
                        Log.e("TO FAV", "ADD")
                    }, {
                        Log.e("ADD TO FAV", it.message)
                    })
            )
        }
    }

    fun deleteFromFavourite() {
        movie?.let {
            execute(
                Completable.fromAction {
                    repository.deleteMovieFromFavourite(it.id)
                }.subscribeOn(Schedulers.io())
                    .subscribe({
                        Log.e("FROM FAV", "DELETE")
                    }, {
                        Log.e("DELETE FROM FAV", it.message)
                    })
            )
        }
    }

    fun isFavourite(): Boolean {
        return isFavourite
    }

    private fun loadReviews(movieId: Int): LiveData<List<Review>> {
        val liveData = MutableLiveData<List<Review>>()
        execute(
            repository.getReviewsById(movieId)
                .compose(RxComposers.applyObservableSchedulers())
                .subscribe({
                    liveData.postValue(it)
                }, {
                    Log.e("REVIEW ERROR", it.message)
                })
        )
        return liveData
    }

    private fun loadTrailers(movieId: Int): LiveData<List<Trailer>> {
        val liveData = MutableLiveData<List<Trailer>>()
        execute(
            repository.getTrailersById(movieId)
                .compose(RxComposers.applyObservableSchedulers())
                .subscribe({
                    liveData.postValue(it)
                },{
                    Log.e("TRAILER ERROR", it.message)
                })
        )
        return liveData
    }

}