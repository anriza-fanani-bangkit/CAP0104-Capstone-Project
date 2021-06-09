package com.example.angkoot.utils.ext

import com.example.angkoot.data.remote.response.DetailPlacesResponse
import com.example.angkoot.data.remote.response.PlacesResponse
import com.example.angkoot.domain.model.Place

fun List<PlacesResponse>.asModel(): List<Place> {
    val places = ArrayList<Place>()

    for (place in this) {
        places.add(
            Place(
                place.id,
                null,
                null,
                null,
                place.description
            )
        )
    }

    return places
}

@JvmName("asModelDetailPlacesResponse")
fun List<DetailPlacesResponse>.asModel(): List<Place> {
    val places = ArrayList<Place>()

    for (place in this) {
        places.add(
            Place(
                place.id,
                place.geometry,
                place.iconUrl,
                place.name,
                null
            )
        )
    }

    return places
}