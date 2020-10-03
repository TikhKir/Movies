package com.example.movies.ui.search

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.example.movies.repository.RepositoryApi
import com.example.movies.repository.model.Movie
import com.example.movies.utils.datatypes.NetworkState
import com.example.movies.utils.rxutils.BaseViewModel
import com.example.movies.utils.rxutils.RxComposers

class SearchViewModel @ViewModelInject constructor(
    private val repository: RepositoryApi,
    @Assisted private val state: SavedStateHandle
): BaseViewModel() {

    private val moviesLiveData = MutableLiveData<List<Movie>>()
    private val networkStateLiveData = MutableLiveData<NetworkState>()

    fun searchMovie(query: String, year: Int?, adult: Boolean) {
        networkStateLiveData.postValue(NetworkState.LOADING)
        execute(
            repository.searchMovie(query, year, adult)
                .compose(RxComposers.applyObservableSchedulers())
                .subscribe({
                    moviesLiveData.postValue(it)
                    networkStateLiveData.postValue(NetworkState.LOADED)
                },{
                    Log.e("SEARCH_GET_ERROR", it.message)
                    networkStateLiveData.postValue(NetworkState.CONNECTION_LOST)
                })
        )
    }

    fun getNetworkLiveData(): LiveData<NetworkState> {
        return networkStateLiveData
    }

    fun getMoviesLiveData(): LiveData<List<Movie>> {
        return moviesLiveData
    }
}