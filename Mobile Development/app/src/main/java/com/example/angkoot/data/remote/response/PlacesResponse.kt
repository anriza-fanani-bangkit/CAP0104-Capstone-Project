package com.example.angkoot.data.remote.response

import com.google.gson.annotations.SerializedName

data class PlacesResponse(
    @SerializedName("place_id")
    val id: String,
    @SerializedName("description")
    val description: String
)
