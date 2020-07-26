package com.example.movies.ui.favourite

import android.app.Application
import com.example.movies.data.model.Movie
import com.example.movies.data.MoviesDatabase
import com.example.movies.utils.rxutils.BaseViewModel
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class FavouriteViewModel(application: Application): BaseViewModel(application) {

    private val database = MoviesDatabase.getInstance(getApplication())


    fun getFavouriteMovies(): Observable<List<Movie>> {
        return database.moviesDao().getAllFavouriteMovies()
            .subscribeOn(Schedulers.io())
    }









}