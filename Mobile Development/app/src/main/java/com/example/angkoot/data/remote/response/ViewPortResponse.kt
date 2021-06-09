package com.example.angkoot.data.remote.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ViewPortResponse(
    @SerializedName("northeast")
    @Expose
    val northeast: LocationResponse,
    @SerializedName("southwest")
    @Expose
    val southwest: LocationResponse,
)
