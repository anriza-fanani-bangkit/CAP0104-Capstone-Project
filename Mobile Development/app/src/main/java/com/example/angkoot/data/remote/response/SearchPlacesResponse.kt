package com.example.angkoot.data.remote.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SearchPlacesResponse(
    @SerializedName("predictions")
    @Expose
    val results: List<PlacesResponse>?
)