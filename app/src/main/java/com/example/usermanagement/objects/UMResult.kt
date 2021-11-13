package com.example.usermanagement.objects

sealed class UMResult<out R> {
    data class Success<out T>(val data: T) : UMResult<T>()
    data class Error(val exception: Throwable) : UMResult<Nothing>()
    object Loading : UMResult<Nothing>()
}

val UMResult<*>.succeeded
    get() = this is UMResult.Success && data != null