package com.example.angkoot.data.remote.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PlacesResponse(
    @SerializedName("place_id")
    @Expose
    val id: String,
    @SerializedName("description")
    @Expose
    val description: String
)
