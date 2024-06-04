package com.anshabunin.eventplanner.data.remote

import android.util.Log
import com.anshabunin.eventplanner.core.domain.model.NetworkResult
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.awaitResponse

suspend fun <T : Any> handleApi(
    execute: suspend () -> Call<T>
): NetworkResult<T>? {
    return try {
        val response = execute().awaitResponse()
        val body = response.body()
        Log.e("ERRROR", "YA TUT")
        when (response.code()) {
            200 -> {
                Log.e("ERRROR", "200")
                if (body != null) NetworkResult.Success(data = body)
                else NetworkResult.ErrorServer(code = 200, msg = "Body is null")

            }
            404 -> {
                Log.e("ERRROR", "404")
                NetworkResult.ErrorServer(code = 404, msg = "Server error")
            }
            else -> {
                Log.e("ERRROR", response.errorBody().toString())
                NetworkResult.ErrorServer(code = response.code(), msg = response.errorBody().toString())
            }
        }
    } catch (e: HttpException) {
        Log.e("ERRROR", e.message().toString())
        NetworkResult.Exception(e)
    } catch (e: Throwable) {
        Log.e("ERRROR", e.toString())
        NetworkResult.Exception(e)
    }
}