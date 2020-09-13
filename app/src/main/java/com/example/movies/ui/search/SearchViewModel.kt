package com.example.movies.ui.search

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movies.repository.RepositoryApiImpl
import com.example.movies.repository.model.Movie
import com.example.movies.utils.datatypes.NetworkState
import com.example.movies.utils.rxutils.BaseViewModel
import com.example.movies.utils.rxutils.RxComposers

class SearchViewModel(application: Application): BaseViewModel(application) {

    private val repository = RepositoryApiImpl(application)
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
                    it.printStackTrace()
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