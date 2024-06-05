package com.anshabunin.eventplanner.data.remote

import com.anshabunin.eventplanner.core.domain.model.NetworkResult
import retrofit2.HttpException
import retrofit2.Response

suspend fun <T : Any> handleApi(
    execute: suspend () -> Response<T>
): NetworkResult<T>? {
    return try {
        val response = execute()
        val body = response.body()
        when (response.code()) {
            200 -> {
                if (body != null) NetworkResult.Success(data = body)
                else NetworkResult.ErrorServer(code = 200, msg = "Body is null")
            }
            404 -> {
                NetworkResult.ErrorServer(code = 404, msg = "Server error")
            }
            else -> {
                NetworkResult.ErrorServer(code = response.code(), msg = response.errorBody().toString())
            }
        }
    } catch (e: HttpException) {
        NetworkResult.Exception(e)
    } catch (e: Throwable) {
        NetworkResult.Exception(e)
    }
}