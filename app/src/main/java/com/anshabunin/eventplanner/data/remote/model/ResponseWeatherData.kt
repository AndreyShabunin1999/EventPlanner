package com.anshabunin.eventplanner.data.remote.model

import com.google.gson.annotations.SerializedName

data class ResponseWeatherData (
    @SerializedName("cod")
    val cod: String?,
    @SerializedName("message")
    val message: Int?,
    @SerializedName("cnt")
    val cnt: Int?,
    @SerializedName("list")
    val list: ArrayList<List> = arrayListOf(),
    @SerializedName("city")
    val city: City?
)