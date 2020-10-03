package com.example.movies.di

import android.content.Context
import androidx.room.Room
import com.example.movies.data.DataApi
import com.example.movies.data.DataApiImpl
import com.example.movies.data.MoviesDatabase
import com.example.movies.data.services.MoviesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object LocalDataModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MoviesDatabase {
        return Room.databaseBuilder(context, MoviesDatabase::class.java, MoviesDatabase.DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideMoviesDao(database: MoviesDatabase): MoviesDao {
        return database.moviesDao()
    }

    @Singleton
    @Provides
    fun provideDataApi(moviesDao: MoviesDao): DataApi {
        return DataApiImpl(moviesDao)
    }


}