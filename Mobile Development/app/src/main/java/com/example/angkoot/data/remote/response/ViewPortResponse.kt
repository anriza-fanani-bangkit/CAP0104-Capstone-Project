package com.example.angkoot.data.remote.response

import com.google.gson.annotations.SerializedName

data class ViewPortResponse(
    @SerializedName("northeast")
    val northeast: LocationResponse,
    @SerializedName("southwest")
    val southwest: LocationResponse,
)
