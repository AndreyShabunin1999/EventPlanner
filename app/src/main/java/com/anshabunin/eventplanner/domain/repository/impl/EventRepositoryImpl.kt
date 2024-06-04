package com.anshabunin.eventplanner.domain.repository.impl

import com.anshabunin.eventplanner.core.data.model.EventStatus
import com.anshabunin.eventplanner.core.database.AppDatabase
import com.anshabunin.eventplanner.core.database.entity.EventEntity
import com.anshabunin.eventplanner.core.domain.model.NetworkResult
import com.anshabunin.eventplanner.data.remote.EventService
import com.anshabunin.eventplanner.data.remote.handleApi
import com.anshabunin.eventplanner.data.remote.model.ResponseWeatherData
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

    override suspend fun updateStatusEvent(idEvent: Int, eventStatus: EventStatus): Int {
        return appDatabase.eventDao().updateStatusEvent(idEvent, eventStatus)
    }

    override suspend fun getWeather(city: String): NetworkResult<ResponseWeatherData>? {
        return handleApi { eventService.getWeather(city) }
    }
}