package com.anshabunin.eventplanner.data.remote

import com.anshabunin.eventplanner.data.remote.model.ResponseWeatherData
import com.anshabunin.eventplanner.utils.KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface EventService {
    @GET("data/2.5/forecast")
    suspend fun getWeather(@Query("q") requestName: String, @Query("appid") appid: String = KEY): Response<ResponseWeatherData>
}