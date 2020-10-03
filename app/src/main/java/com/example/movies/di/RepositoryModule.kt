package com.example.movies.di

import com.example.movies.data.DataApi
import com.example.movies.network.NetworkApi
import com.example.movies.repository.RepositoryApi
import com.example.movies.repository.RepositoryApiImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideRepository(networkApi: NetworkApi, dataApi: DataApi): RepositoryApi {
        return RepositoryApiImpl(networkApi, dataApi)
    }

}