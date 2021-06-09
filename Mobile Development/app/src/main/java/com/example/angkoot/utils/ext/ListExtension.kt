package com.example.angkoot.utils.ext

fun List<Boolean>.isAllTrue(): Boolean {
    var state = true

    for (isValueTrue in this) {
        if (!isValueTrue) {
            state = false
            break
        }
    }

    return state
}