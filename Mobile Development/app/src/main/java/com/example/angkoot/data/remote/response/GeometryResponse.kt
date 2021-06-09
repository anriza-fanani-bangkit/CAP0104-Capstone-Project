package com.example.angkoot.data.remote.response

import com.google.gson.annotations.SerializedName

data class GeometryResponse(
    @SerializedName("location")
    val location: LocationResponse,
    @SerializedName("viewport")
    val viewPort: ViewPortResponse
)
