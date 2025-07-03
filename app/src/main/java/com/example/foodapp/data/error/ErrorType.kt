package com.example.foodapp.data.error

sealed class ErrorType {
    object NetworkError : ErrorType()
    object ServerError : ErrorType()
    object UnknownError : ErrorType()
    data class ApiError(val code: Int, val message: String) : ErrorType()
    data class ValidationError(val field: String, val message: String) : ErrorType()
    
    fun getDisplayMessage(): String {
        return when (this) {
            is NetworkError -> "Please check your internet connection"
            is ServerError -> "Server is currently unavailable. Please try again later"
            is ApiError -> message
            is ValidationError -> "$field: $message"
            is UnknownError -> "An unexpected error occurred"
        }
    }
}