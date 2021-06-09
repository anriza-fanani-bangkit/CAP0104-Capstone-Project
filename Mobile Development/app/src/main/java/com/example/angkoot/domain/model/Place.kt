package com.example.angkoot.domain.model

import com.example.angkoot.data.remote.response.GeometryResponse

data class Place(
    val id: String,
    val geometry: GeometryResponse?,
    val iconUrl: String?,
    val name: String?,
    val description: String?
)
