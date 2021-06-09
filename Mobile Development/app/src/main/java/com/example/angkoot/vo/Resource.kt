package com.example.angkoot.vo

data class Resource<out T>(
    val status: StatusRes,
    val data: T?,
    val message: String?
) {
    companion object {
        fun <T> success(data: T): Resource<T> =
            Resource(StatusRes.SUCCESS, data, null)

        fun <T> error(data: T?, message: String): Resource<T> =
            Resource(StatusRes.ERROR, data, message)

        fun <T> loading(data: T?): Resource<T> =
            Resource(StatusRes.LOADING, data, null)
    }
}
