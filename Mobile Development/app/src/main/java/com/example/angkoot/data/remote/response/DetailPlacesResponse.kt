package com.example.angkoot.data.remote.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DetailPlacesResponse(
    @SerializedName("place_id")
    @Expose
    val id: String,
    @SerializedName("geometry")
    @Expose
    val geometry: GeometryResponse,
    @SerializedName("icon")
    @Expose
    val iconUrl: String?,
    @SerializedName("name")
    @Expose
    val name: String
)
