package com.anshabunin.eventplanner.domain.repository.impl

import android.util.Log
import com.anshabunin.eventplanner.core.database.AppDatabase
import com.anshabunin.eventplanner.core.database.entity.EventEntity
import com.anshabunin.eventplanner.data.remote.EventService
import com.anshabunin.eventplanner.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow

class EventRepositoryImpl(
    private val eventService: EventService,
    private val appDatabase: AppDatabase
) : EventRepository {
    override fun getEvents(): Flow<List<EventEntity>> {
        return appDatabase.eventDao().getEventsList()
    }

    override suspend fun updateEvent(event: EventEntity): Int {
        return appDatabase.eventDao().updateEvent(event)
    }

    override suspend fun deleteEvent(event: EventEntity): Int {
        return appDatabase.eventDao().deleteEvent(event)
    }

    override suspend fun insertEvent(event: EventEntity): Long {
        return appDatabase.eventDao().insertEvent(event)
    }
}