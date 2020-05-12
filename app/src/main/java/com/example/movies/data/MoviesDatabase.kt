package com.example.movies.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Movie::class, FavouriteMovie::class], version = 2, exportSchema = false)
abstract class MoviesDatabase: RoomDatabase() {

    companion object {
        private var database: MoviesDatabase? = null
        private const val DB_NAME = "moviesRoom.db"
        private val LOCK = Any()

        fun getInstance(context: Context): MoviesDatabase {
            synchronized(LOCK) {
                database?.let { return it}
                val instance =
                    Room.databaseBuilder(context, MoviesDatabase::class.java, DB_NAME)
                        .fallbackToDestructiveMigration()
                        .build()
                database = instance
                return instance
            }
        }
    }

    abstract fun moviesDao(): MoviesDao
}