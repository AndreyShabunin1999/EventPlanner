package com.anshabunin.eventplanner.utils

import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.abs

 fun convertDateFormat(inputDate: String): String? {
    val inputFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date = inputFormat.parse(inputDate)
    return date?.let { outputFormat.format(it) }
}

 fun findNearestTime(targetTime: String): String? {
    val availableTimes = listOf("00:00:00", "03:00:00", "06:00:00", "09:00:00", "12:00:00", "15:00:00", "18:00:00", "21:00:00")
    val (targetHour, targetMinute) = targetTime.split(":").map { it.toInt() }
    val targetTotalMinutes = targetHour * 60 + targetMinute

    val nearestTime = availableTimes.minByOrNull { availableTime ->
        val (availableHour, availableMinute) = availableTime.split(":").map { it.toInt() }
        val availableTotalMinutes = availableHour * 60 + availableMinute
        val difference = abs(availableTotalMinutes - targetTotalMinutes)
        difference
    }

    return nearestTime
}