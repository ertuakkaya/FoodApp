package com.example.foodapp.data

sealed class ResourceState <T> {

    class Loading<T> : ResourceState<T>()
    data class Success<T>(val data: T) : ResourceState<T>()
    data class Error<T>(val error: String) : ResourceState<T>()

}

sealed class ResourceStateSepeteEkle <T> {

    class Loading<T> : ResourceStateSepeteEkle<T>()
    data class Success<T>(val data: T) : ResourceStateSepeteEkle<T>()
    data class Error<T>(val error: String) : ResourceStateSepeteEkle<T>()

}