package com.example.movies

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.movies.data.Movie
import com.example.movies.data.MoviesDatabase
import com.example.movies.utils.rxutils.BaseViewModel
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class FavouriteViewModel(application: Application): BaseViewModel(application) {

    private val database = MoviesDatabase.getInstance(getApplication())


    fun getFavouriteMovies(): Observable<List<Movie>> {
        return database.moviesDao().getAllFavouriteMovies()
            .subscribeOn(Schedulers.io())
    }









}