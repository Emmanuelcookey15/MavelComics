package com.emmanuel.cookey.marvelcomics.data.model

sealed class Resource<T>(
    val status: Status,
    val data: T? = null,
    val error: String? = null
) {
    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }


    class Success<T>(data: T) : Resource<T>(Status.SUCCESS, data)
    class Loading<T>(data: T? = null) : Resource<T>(Status.LOADING, data)
    class Error<T>(exception: String, data: T? = null) : Resource<T>(Status.ERROR, data, exception)
}
