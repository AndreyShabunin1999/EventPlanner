package com.anshabunin.eventplanner.data.remote.model

import com.google.gson.annotations.SerializedName


data class List (
    @SerializedName("dt")
    val dt: Int?,
    @SerializedName("main")
    val main: Main?,
    @SerializedName("weather")
    val weather: ArrayList<Weather>,
    @SerializedName("clouds")
    val clouds: Clouds?,
    @SerializedName("wind")
    val wind: Wind?,
    @SerializedName("visibility")
    val visibility : Int?,
    @SerializedName("pop")
    val pop: Double?,
    @SerializedName("rain")
    val rain: Rain?,
    @SerializedName("sys")
    val sys : Sys?,
    @SerializedName("dt_txt")
    val dtTxt: String?
)
