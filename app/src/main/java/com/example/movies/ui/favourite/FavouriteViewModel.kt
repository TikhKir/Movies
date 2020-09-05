package com.example.movies.ui.favourite

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movies.repository.RepositoryApiImpl
import com.example.movies.repository.model.Movie
import com.example.movies.utils.rxutils.BaseViewModel
import com.example.movies.utils.rxutils.RxComposers

class FavouriteViewModel(application: Application) : BaseViewModel(application) {

    private val repository = RepositoryApiImpl(application)

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