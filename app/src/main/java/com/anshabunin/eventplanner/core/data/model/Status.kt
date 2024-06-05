package com.anshabunin.eventplanner.core.data.model

import android.content.Context
import com.anshabunin.eventplanner.R

enum class EventStatus {
    UPCOMING,
    ATTENDED,
    MISSED
}

fun EventStatus.getValueStatus(context: Context?): String? {
    val statusVisit: Array<String>? = context?.resources?.getStringArray(R.array.status_visit)

    return when (this) {
        EventStatus.UPCOMING -> statusVisit?.getOrNull(0)
        EventStatus.ATTENDED -> statusVisit?.getOrNull(1)
        EventStatus.MISSED -> statusVisit?.getOrNull(2)
    }
}

fun String.getEnumStatus(context: Context?): EventStatus? {
    val statusVisit: Array<String>? = context?.resources?.getStringArray(R.array.status_visit)
    return when (this) {
        statusVisit?.getOrNull(0) -> EventStatus.UPCOMING
        statusVisit?.getOrNull(1) -> EventStatus.ATTENDED
        statusVisit?.getOrNull(2) -> EventStatus.MISSED
        else -> {return null}
    }
}