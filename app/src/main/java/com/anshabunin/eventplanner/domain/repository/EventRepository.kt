package com.anshabunin.eventplanner.domain.repository

import com.anshabunin.eventplanner.core.database.entity.EventEntity
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    suspend fun getEvents(): Flow<List<EventEntity>?>

    suspend fun updateEvent(event: EventEntity): Int

    suspend fun deleteEvent(idEvent: Int): Int
    suspend fun insertEvent(event: EventEntity): Long

}