package com.example.angkoot.data.remote.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LocationResponse(
    @SerializedName("lat")
    @Expose
    val latitude: Double,
    @SerializedName("lng")
    @Expose
    val longitude: Double,
)
