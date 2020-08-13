package com.example.movies.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.movies.data.model.Movie
import com.example.movies.data.services.MoviesDao

@Database(entities = [Movie::class], version = 11, exportSchema = false)
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