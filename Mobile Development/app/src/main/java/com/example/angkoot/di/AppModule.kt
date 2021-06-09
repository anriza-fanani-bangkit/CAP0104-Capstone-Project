package com.example.angkoot.di

import com.example.angkoot.api.ApiEndpoint
import com.example.angkoot.api.ApiEndpoint.Companion.BASE_URL
import com.example.angkoot.data.AngkootRepository
import com.example.angkoot.data.remote.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): ApiEndpoint =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiEndpoint::class.java)

    @Provides
    @Singleton
    fun provideRemoteDataSource(
        apiClient: ApiEndpoint
    ): RemoteDataSource =
        RemoteDataSource(apiClient)

    @Provides
    @Singleton
    fun provideRepository(
        remote: RemoteDataSource
    ): AngkootRepository =
        AngkootRepository(remote)
}