package com.example.angkoot.data.remote.response

import com.google.gson.annotations.SerializedName

data class SearchPlacesResponse(
    @SerializedName("predictions")
    val results: List<PlacesResponse>?
)