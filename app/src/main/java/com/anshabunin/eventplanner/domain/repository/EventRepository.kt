package com.anshabunin.eventplanner.domain.repository

import com.anshabunin.eventplanner.core.database.entity.EventEntity
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    fun getEvents(): Flow<List<EventEntity>>

    suspend fun updateEvent(event: EventEntity): Int

    suspend fun deleteEvent(event: EventEntity): Int
    suspend fun insertEvent(event: EventEntity): Long

}