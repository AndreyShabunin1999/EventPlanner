package com.anshabunin.eventplanner.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.anshabunin.eventplanner.core.data.model.EventStatus
import com.anshabunin.eventplanner.utils.RANDOM_IMAGE

@Entity(tableName = "event")
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo
    val title: String,
    @ColumnInfo
    val description: String,
    @ColumnInfo
    val date: String,
    @ColumnInfo
    val location: String,
    @ColumnInfo
    val city: String,
    @ColumnInfo
    var imageUrl: String = RANDOM_IMAGE,
    @ColumnInfo
    val status: EventStatus = EventStatus.UPCOMING
)