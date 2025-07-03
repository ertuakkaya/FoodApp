package com.example.foodapp.data

import com.example.foodapp.data.error.ErrorType

sealed class ResourceState <T> {

    class Loading<T> : ResourceState<T>()
    data class Success<T>(val data: T) : ResourceState<T>()
    data class Error<T>(val error: String, val errorType: ErrorType = ErrorType.UnknownError) : ResourceState<T>()


}