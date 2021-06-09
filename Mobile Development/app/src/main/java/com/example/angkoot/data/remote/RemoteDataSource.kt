package com.example.angkoot.data.remote

import com.example.angkoot.api.ApiEndpoint
import com.example.angkoot.utils.ext.asModel
import com.example.angkoot.vo.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val api: ApiEndpoint
) {

    suspend fun searchPlaces(query: String) = flow {
        emit(Resource.loading(null))

        try {
            val callResults = api.searchPlaces(query)
            val data = callResults.body()

            if (callResults.isSuccessful && data != null) {
                emit(Resource.success(data.results?.asModel()))
            } else {
                emit(Resource.error(null, callResults.message()))
            }
        } catch (exc: Exception) {
            emit(Resource.error(null, exc.message ?: "Error occurred!"))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getDetailPlacesOf(placeId: String) = flow {
        emit(Resource.loading(null))

        try {
            val callResults = api.getDetailPlaceOf(placeId)
            val data = callResults.body()

            if (callResults.isSuccessful && data != null) {
                emit(Resource.success(data.results?.asModel()))
            } else {
                emit(Resource.error(null, callResults.message()))
            }
        } catch (exc: Exception) {
            emit(Resource.error(null, exc.message ?: "Error occurred!"))
        }
    }.flowOn(Dispatchers.IO)
}