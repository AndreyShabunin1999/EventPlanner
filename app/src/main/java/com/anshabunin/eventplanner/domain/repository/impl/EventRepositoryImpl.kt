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

    override fun getEvent(idEvent: Int): Flow<EventEntity> {
        return appDatabase.eventDao().getEvent(idEvent)
    }

    override suspend fun updateEvent(event: EventEntity): Int {
        return appDatabase.eventDao().updateEvent(event)
    }

    override suspend fun deleteEvent(idEvent: Int): Int {
        return appDatabase.eventDao().deleteEvent(idEvent)
    }

    override suspend fun insertEvent(event: EventEntity): Long {
        return appDatabase.eventDao().insertEvent(event)
    }
}