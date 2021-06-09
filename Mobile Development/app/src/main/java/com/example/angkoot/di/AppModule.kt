package com.example.angkoot.di

import com.example.angkoot.api.ApiEndpoint
import com.example.angkoot.api.ApiEndpoint.Companion.BASE_URL
import com.example.angkoot.api.ApiEndpoint.Companion.hostname
import com.example.angkoot.data.AngkootRepository
import com.example.angkoot.data.remote.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .certificatePinner(
                CertificatePinner.Builder()
                    .add(hostname, "sha256/Q7MIEWtOcNj+1rFg57yHxJRRwNG9xGNxPQUt/LulvbM=")
                    .add(hostname, "sha256/YZPgTZ+woNCCCIW3LH2CxQeLzB/1m42QcCTBSdgayjs=")
                    .build()
            )
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(
        client: OkHttpClient
    ): ApiEndpoint =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
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