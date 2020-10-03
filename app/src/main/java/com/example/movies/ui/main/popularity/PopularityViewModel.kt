package com.example.movies.ui.main.popularity

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.example.movies.constants.SortTypes
import com.example.movies.repository.RepositoryApi
import com.example.movies.repository.model.Movie
import com.example.movies.utils.datatypes.NetworkState
import com.example.movies.utils.datatypes.Result
import com.example.movies.utils.datatypes.ResultType
import com.example.movies.utils.rxutils.BaseViewModel
import com.example.movies.utils.rxutils.RxComposers

class PopularityViewModel @ViewModelInject constructor(
    private val repository: RepositoryApi,
    @Assisted private val state: SavedStateHandle
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
        networkStateLiveData.postValue(NetworkState.LOADING)
        execute(
            repository.getMoviesPage(SortTypes.POPULARITY, page)
                .compose(RxComposers.applyObservableSchedulers())
                .subscribe({
                    popularityResponseManager(it)
//                    it.data?.map {
//                        Log.e("VM GETPOP", " ${it.title} #${it.id}" )
//                    }
                }, {
                    Log.e("VM POP ERROR", it.message)
                    networkStateLiveData.postValue(NetworkState.CONNECTION_LOST)
                }, {
                    Log.e("GETPOP", "COMPLETE")
                    if (listIsEmpty()) networkStateLiveData.postValue(NetworkState.CONNECTION_LOST)
                })
        )
    }


    private fun popularityResponseManager(result: Result<List<Movie>>) {
        when {
            result.resultType == ResultType.FROM_NW && previousResult == ResultType.FROM_NW -> {
                moviesCumulativeList.addAll(result.data as List<Movie>)
                moviesLiveData.postValue(moviesCumulativeList)
                Log.e("###", "NW -> NW #$page")
                page++
                networkStateLiveData.postValue(NetworkState.LOADED)
            }

            result.resultType == ResultType.FROM_NW && previousResult == ResultType.INIT -> {
                moviesCumulativeList.addAll(result.data as List<Movie>)
                moviesLiveData.postValue(moviesCumulativeList)
                Log.e("###", "INIT -> NW #$page")
                page++
                networkStateLiveData.postValue(NetworkState.LOADED)
            }

            result.resultType == ResultType.FROM_NW && previousResult == ResultType.FROM_DB -> {
                moviesCumulativeList.clear()
                moviesCumulativeList.addAll(result.data as List<Movie>)
                moviesLiveData.postValue(moviesCumulativeList)
                Log.e("###", "DB -> NW #$page")
                networkStateLiveData.postValue(NetworkState.LOADED)
            }

            result.resultType == ResultType.FROM_DB && previousResult == ResultType.FROM_DB -> {
                // пока бесполезно но пусть повисит
                Log.e("###", "DB -> DB #$page")
                page = 1
            }

            result.resultType == ResultType.FROM_DB && previousResult == ResultType.FROM_NW -> {
                moviesCumulativeList.clear()
                moviesCumulativeList.addAll(result.data as List<Movie>)
                moviesLiveData.postValue(moviesCumulativeList)
                Log.e("###", "NW -> DB #$page")
                networkStateLiveData.postValue(NetworkState.CONNECTION_LOST)
                page = 1
            }

            result.resultType == ResultType.FROM_DB && previousResult == ResultType.INIT -> {
                moviesCumulativeList.addAll(result.data as List<Movie>)
                moviesLiveData.postValue(moviesCumulativeList)
                Log.e("###", "INIT -> DB #$page")
                networkStateLiveData.postValue(NetworkState.CONNECTION_LOST)
                page = 1
            }

            else -> {
                moviesCumulativeList.addAll(result.data as List<Movie>)
                moviesLiveData.postValue(moviesCumulativeList)
                networkStateLiveData.postValue(NetworkState.LOADED)
                Log.e("###", "ELSE #$page")
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