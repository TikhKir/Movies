package com.example.movies.di

import com.example.movies.constants.BaseUrl
import com.example.movies.network.NetworkApi
import com.example.movies.network.NetworkApiImpl
import com.example.movies.network.services.BaseService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(BaseUrl.BASE_URL.url)
            .build()
    }

    @Singleton
    @Provides
    fun provideBaseService(retrofit: Retrofit): BaseService {
        return retrofit.create(BaseService::class.java)
    }

    @Singleton
    @Provides
    fun provideNetworkApi(baseService: BaseService): NetworkApi {
        return NetworkApiImpl(baseService)
    }

}