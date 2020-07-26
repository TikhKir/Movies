package com.example.movies.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movies.constants.SortTypes
import com.example.movies.data.model.Movie
import com.example.movies.data.MoviesDatabase
import com.example.movies.repository.RepositoryApiImpl
import com.example.movies.utils.rxutils.BaseViewModel
import com.example.movies.utils.rxutils.RxComposers

class PopularityViewModel(application: Application) : BaseViewModel(application) {

    private val database = MoviesDatabase.getInstance(getApplication())
    private val repository = RepositoryApiImpl(getApplication())

    val popularityLiveData = MutableLiveData<List<Movie>>()
    val topRatedLiveData = MutableLiveData<List<Movie>>()

    var pageTopRated: Int = 1
    var pagePopularity: Int = 1

    init {
        loadPopularity()
        loadTopRated()
    }

    fun loadPopularity() {
        execute(
            repository.getMoviesPage(SortTypes.POPULARITY, pagePopularity)
                .compose(RxComposers.applyObservableSchedulers())
                .subscribe({
                    it.map {
                        Log.e("VM GETPOP", " ${it.title} #${it.localId}" )
                    }
                    popularityLiveData.postValue(it)
                },{
                    Log.e("VM POP ERROR", it.message)
                },{
                    Log.e("GETPOP", "COMPLETE" )
                })
        )
    }

    fun loadTopRated() {
        execute(
            repository.getMoviesPage(SortTypes.TOP_RATED, pageTopRated)
                .compose(RxComposers.applyObservableSchedulers())
                .subscribe({
                    topRatedLiveData.postValue(it)
                },{
                    Log.e("VM TOP_R", it.message)
                },{
                    Log.e("VM TOP_R", "Complete")
                })
        )
    }




    fun getPopularityLiveData(): LiveData<List<Movie>> {
        return popularityLiveData
    }

    fun getTopRatedLiveData(): LiveData<List<Movie>> {
        return topRatedLiveData
    }







}