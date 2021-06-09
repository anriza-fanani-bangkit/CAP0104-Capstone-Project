package com.example.angkoot.data

import androidx.lifecycle.asLiveData
import com.example.angkoot.data.remote.RemoteDataSource
import javax.inject.Inject

class AngkootRepository @Inject constructor(
    private val remote: RemoteDataSource
) {
    suspend fun searchPlaces(query: String) =
        remote.searchPlaces(query)

    suspend fun getDetailPlacesOf(placeId: String) =
        remote.getDetailPlacesOf(placeId).asLiveData()
}