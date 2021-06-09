package com.example.angkoot.data.remote.response

import com.google.gson.annotations.SerializedName

data class DetailPlacesResponse(
    @SerializedName("place_id")
    val id: String,
    @SerializedName("geometry")
    val geometry: GeometryResponse,
    @SerializedName("icon")
    val iconUrl: String?,
    @SerializedName("name")
    val name: String
)
