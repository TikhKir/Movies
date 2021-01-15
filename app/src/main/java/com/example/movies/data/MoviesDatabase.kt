package com.example.movies.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.movies.data.model.MovieFavourite
import com.example.movies.data.model.MoviePopDB
import com.example.movies.data.model.MovieTopDB
import com.example.movies.data.services.MoviesDao

@Database(
    entities = [
        MovieTopDB::class,
        MoviePopDB::class,
        MovieFavourite::class
    ],
    version = 14,
    exportSchema = false
)
abstract class MoviesDatabase : RoomDatabase() {

    companion object {
        const val DB_NAME = "moviesRoom.db"
    }

    abstract fun moviesDao(): MoviesDao
}