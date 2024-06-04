package com.anshabunin.eventplanner.core.domain.model

sealed class NetworkResult<out T> {
    class Success<T>(val data: T) : NetworkResult<T>()
    class ErrorServer<T>(val code: Int, val msg: String) : NetworkResult<T>()
    class Exception<T>(val e: Throwable) : NetworkResult<T>()
}

suspend fun <T : Any> NetworkResult<T>.onSuccess(
    executable: suspend (T) -> Unit
): NetworkResult<T> = apply {
    if (this is NetworkResult.Success<T>) {
        executable (data)
    }
}

suspend fun <T : Any> NetworkResult<T>.onErrorServer(
    executable: suspend (code: Int, msg: String) -> Unit
): NetworkResult<T> = apply {
    if (this is NetworkResult.ErrorServer<T>) {
        executable(code, msg)
    }
}

suspend fun <T : Any> NetworkResult<T>.onException(
    executable: suspend (e: Throwable) -> Unit
): NetworkResult<T> = apply {
    if (this is NetworkResult.Exception<T>) {
        executable(e)
    }
}