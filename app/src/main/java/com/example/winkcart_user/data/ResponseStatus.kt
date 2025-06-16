package com.example.winkcart_user.data

import kotlinx.serialization.Serializable

sealed class ResponseStatus<out T> {
    object Loading : ResponseStatus<Nothing>()
    data class Success<T>(val result: T) : ResponseStatus<T>()
    data class Error(val error: Throwable) : ResponseStatus<Nothing>()
}
