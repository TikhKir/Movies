package com.example.movies

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.movies.data.Movie
import com.example.movies.data.MoviesDatabase

class FavouriteViewModel(application: Application): AndroidViewModel(application) {

    private val database = MoviesDatabase.getInstance(getApplication())
    private lateinit var favouriteMoviesLiveData : LiveData<List<Movie>>

    fun getFavouriteMoviesFromDB(): LiveData<List<Movie>> {
        favouriteMoviesLiveData = database.moviesDao().getAllFavouriteMovies()
        return favouriteMoviesLiveData
    }











}