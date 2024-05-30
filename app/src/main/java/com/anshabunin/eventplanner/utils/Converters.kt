package com.anshabunin.eventplanner.utils

import androidx.room.TypeConverter
import com.anshabunin.eventplanner.core.data.model.EventStatus

class Converters {

    @TypeConverter
    fun fromEventStatus(status: EventStatus): String {
        return status.name
    }

    @TypeConverter
    fun toEventStatus(status: String): EventStatus {
        return EventStatus.valueOf(status)
    }
}