package com.example.movies.ui.favourite

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.example.movies.repository.RepositoryApi
import com.example.movies.repository.model.Movie
import com.example.movies.utils.rxutils.BaseViewModel
import com.example.movies.utils.rxutils.RxComposers

class FavouriteViewModel @ViewModelInject constructor(
    private val repository: RepositoryApi,
    @Assisted private val state: SavedStateHandle
) : BaseViewModel() {

    fun loadFavourite(): LiveData<List<Movie>> {
        val liveData = MutableLiveData<List<Movie>>()
        execute(
            repository.getFavouriteMovies()
                .compose(RxComposers.applyObservableSchedulers())
                .subscribe({
                    liveData.postValue(it)
                }, {
                    Log.e("FAVOURITE ERROR", it.message)
                })
        )
        return liveData
    }


}