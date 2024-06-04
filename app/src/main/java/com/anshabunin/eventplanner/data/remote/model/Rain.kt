package com.anshabunin.eventplanner.data.remote.model

import com.google.gson.annotations.SerializedName


data class Rain (
    @SerializedName("3h")
    val threeH : Double?
)
