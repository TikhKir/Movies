package com.example.movies.ui.detail

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movies.repository.RepositoryApi
import com.example.movies.repository.model.Movie
import com.example.movies.repository.model.Review
import com.example.movies.repository.model.Trailer
import com.example.movies.utils.datatypes.NetworkState
import com.example.movies.utils.datatypes.ResultType
import com.example.movies.utils.rxutils.BaseViewModel
import com.example.movies.utils.rxutils.RxComposers
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers

class DetailViewModel @ViewModelInject constructor(
    private val repository: RepositoryApi
) : BaseViewModel() {

    val liveDataReview: LiveData<List<Review>> by lazy { loadReviews(movieId) }
    val liveDataTrailers: LiveData<List<Trailer>> by lazy { loadTrailers(movieId) }
    val liveDataNetworkState = MutableLiveData<NetworkState>()

    var movieId = 0
    private var isFavourite = false
    private var movie: Movie? = null


    fun loadMovie(movieId: Int): LiveData<Movie> {
        val liveDataMovie = MutableLiveData<Movie>()
        liveDataNetworkState.value = NetworkState.LOADING
        execute(
            repository.getMovieById(movieId)
                .compose(RxComposers.applyObservableSchedulers())
                .subscribe({
                    when (it.resultType) {
                        ResultType.MOVIE_NOT_FAV,
                        ResultType.FROM_NW -> isFavourite = false
                        ResultType.MOVIE_FAV -> isFavourite = true
                    }
                    liveDataMovie.value = it.data
                    movie = it.data
                    liveDataNetworkState.value = NetworkState.LOADED
                }, {
                    Log.e("LOAD MOVIE ERROR", it.message)
                    liveDataNetworkState.value = NetworkState.CONNECTION_LOST
                })
        )
        return liveDataMovie
    }

    fun addMovieToFavourite() {
        movie?.let {
            execute(
                Completable.fromAction {
                    repository.addMovieToFavourite(it)
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
                    liveData.value = it
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
                    liveData.value = it
                },{
                    Log.e("TRAILER ERROR", it.message)
                })
        )
        return liveData
    }

}