package com.example.movies.ui.main.popularity

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movies.constants.SortTypes
import com.example.movies.repository.RepositoryApi
import com.example.movies.repository.model.Movie
import com.example.movies.utils.datatypes.NetworkState
import com.example.movies.utils.datatypes.Result
import com.example.movies.utils.datatypes.ResultType
import com.example.movies.utils.rxutils.BaseViewModel
import com.example.movies.utils.rxutils.RxComposers

class PopularityViewModel @ViewModelInject constructor(
    private val repository: RepositoryApi
) : BaseViewModel() {


    private val moviesLiveData = MutableLiveData<List<Movie>>()
    private var moviesCumulativeList = mutableListOf<Movie>()
    private val networkStateLiveData = MutableLiveData<NetworkState>()
    private var previousResult = ResultType.INIT
    private var page: Int = 1

    init {
        loadPopularity()
    }

    fun loadPopularity() {
        networkStateLiveData.value = NetworkState.LOADING
        execute(
            repository.getMoviesPage(SortTypes.POPULARITY, page)
                .compose(RxComposers.applyObservableSchedulers())
                .subscribe({
                    popularityResponseManager(it)
                }, {
                    Log.e("VM POP ERROR", it.message)
                    networkStateLiveData.value = NetworkState.CONNECTION_LOST
                }, {
                    if (listIsEmpty()) networkStateLiveData.value = NetworkState.CONNECTION_LOST
                })
        )
    }


    private fun popularityResponseManager(result: Result<List<Movie>>) {
        when {
            result.resultType == ResultType.FROM_NW && previousResult == ResultType.FROM_NW -> {
                moviesCumulativeList.addAll(result.data as List<Movie>)
                moviesLiveData.value = moviesCumulativeList
                Log.e("POP_RESPONSE_MANAGE", "NW -> NW #$page")
                page++
                networkStateLiveData.value = NetworkState.LOADED
            }

            result.resultType == ResultType.FROM_NW && previousResult == ResultType.INIT -> {
                moviesCumulativeList.addAll(result.data as List<Movie>)
                moviesLiveData.value = moviesCumulativeList
                Log.e("POP_RESPONSE_MANAGE", "INIT -> NW #$page")
                page++
                networkStateLiveData.value = NetworkState.LOADED
            }

            result.resultType == ResultType.FROM_NW && previousResult == ResultType.FROM_DB -> {
                moviesCumulativeList.clear()
                moviesCumulativeList.addAll(result.data as List<Movie>)
                moviesLiveData.value = moviesCumulativeList
                Log.e("POP_RESPONSE_MANAGE", "DB -> NW #$page")
                networkStateLiveData.value = NetworkState.LOADED
            }

            result.resultType == ResultType.FROM_DB && previousResult == ResultType.FROM_DB -> {
                Log.e("POP_RESPONSE_MANAGE", "DB -> DB #$page")
                page = 1
            }

            result.resultType == ResultType.FROM_DB && previousResult == ResultType.FROM_NW -> {
                moviesCumulativeList.clear()
                moviesCumulativeList.addAll(result.data as List<Movie>)
                moviesLiveData.value = moviesCumulativeList
                Log.e("POP_RESPONSE_MANAGE", "NW -> DB #$page")
                networkStateLiveData.value = NetworkState.CONNECTION_LOST
                page = 1
            }

            result.resultType == ResultType.FROM_DB && previousResult == ResultType.INIT -> {
                moviesCumulativeList.addAll(result.data as List<Movie>)
                moviesLiveData.value = moviesCumulativeList
                Log.e("POP_RESPONSE_MANAGE", "INIT -> DB #$page")
                networkStateLiveData.value = NetworkState.CONNECTION_LOST
                page = 1
            }

            else -> {
                moviesCumulativeList.addAll(result.data as List<Movie>)
                moviesLiveData.value = moviesCumulativeList
                networkStateLiveData.value = NetworkState.LOADED
                Log.e("POP_RESPONSE_MANAGE", "ELSE #$page")
                page = 1
            }
        }
        previousResult = result.resultType
    }

    fun getMoviesLiveData(): LiveData<List<Movie>> {
        return moviesLiveData
    }

    fun getNetworkStateLiveData(): LiveData<NetworkState> {
        return networkStateLiveData
    }

    fun listIsEmpty(): Boolean {
        return moviesCumulativeList.isEmpty()
    }

}