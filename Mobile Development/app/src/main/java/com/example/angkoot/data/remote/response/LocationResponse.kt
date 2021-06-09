package com.example.angkoot.data.remote.response

import com.google.gson.annotations.SerializedName

data class LocationResponse(
    @SerializedName("lat")
    val latitude: Double,
    @SerializedName("lng")
    val longitude: Double,
)
