package com.example.angkoot.api

import com.example.angkoot.BuildConfig
import com.example.angkoot.data.remote.response.GetDetailPlacesResponse
import com.example.angkoot.data.remote.response.SearchPlacesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiEndpoint {
    @Headers("Accept-Encoding: identity")
    @GET("${PATH}/autocomplete/json")
    suspend fun searchPlaces(
        @Query("input") query: String,
        @Query("radius") radius: Int = 500,
        @Query("key") apiKey: String = MAP_API_KEY
    ): Response<SearchPlacesResponse>

    @Headers("Accept-Encoding: identity")
    @GET("${PATH}/details/json")
    suspend fun getDetailPlaceOf(
        @Query("placeid") id: String,
        @Query("key") apiKey: String = MAP_API_KEY
    ): Response<GetDetailPlacesResponse>

    companion object {
        const val BASE_URL = "https://maps.googleapis.com/"
        const val PATH = "maps/api/place"
        const val MAP_API_KEY = BuildConfig.MAPS_API_KEY
        const val hostname = "maps.googleapis.com"
    }
}