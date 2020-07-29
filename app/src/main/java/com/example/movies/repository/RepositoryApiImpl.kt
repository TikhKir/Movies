package com.example.movies.repository

import android.app.Application
import android.util.Log
import com.example.movies.constants.SortTypes
import com.example.movies.data.DataApiImpl
import com.example.movies.data.model.Movie
import com.example.movies.network.NetworkApiImpl
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class RepositoryApiImpl(application: Application): RepositoryApi {

    val networkSource = NetworkApiImpl()
    val databaseSource = DataApiImpl(application)


    override fun getMoviesPage(sortTypes: SortTypes, page: Int): Observable<List<Movie>> {
        return  getMoviesFromNetworkWithCaching(sortTypes, page)
            .startWith(databaseSource.getMoviesBySearchMethod(sortTypes).toObservable())
            .onErrorResumeNext(databaseSource.getMoviesBySearchMethod(sortTypes).toObservable())
    }

    private fun getMoviesFromNetworkWithCaching(sortTypes: SortTypes, page: Int): Observable<List<Movie>> {
        return networkSource.getMovies(sortTypes, page)
            .observeOn(Schedulers.single())
            .doOnSuccess {
                it.map {
                    databaseSource.upsertMovie(it, sortTypes)
                }
            }
            .observeOn(Schedulers.io())
            .toObservable()
    }





    override fun getMovieById(movieId: Int): Observable<Movie> {
        TODO("Not yet implemented")
    }

    //private fun saveMovieToCache(movie: Movie, sortTypes: SortTypes)

    //private fun getMoviesFromCache(sortTypes: SortTypes): Observable<List<Movie>>

    //получать с бд все сразу скопом а из сети постранично, стирать данные из бд приполучении актуальных
}