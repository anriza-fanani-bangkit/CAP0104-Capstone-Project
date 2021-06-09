package com.example.angkoot.data.remote.response

import com.google.gson.annotations.SerializedName

data class GetDetailPlacesResponse(
    @SerializedName("result")
    val results: List<DetailPlacesResponse>?
)