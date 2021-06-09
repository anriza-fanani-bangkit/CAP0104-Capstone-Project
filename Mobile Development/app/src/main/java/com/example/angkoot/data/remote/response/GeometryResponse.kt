package com.example.angkoot.data.remote.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GeometryResponse(
    @SerializedName("location")
    @Expose
    val location: LocationResponse,
    @Expose
    @SerializedName("viewport")
    val viewPort: ViewPortResponse
)
