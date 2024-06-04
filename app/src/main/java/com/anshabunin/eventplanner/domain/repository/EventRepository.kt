package com.anshabunin.eventplanner.domain.repository

import com.anshabunin.eventplanner.core.data.model.EventStatus
import com.anshabunin.eventplanner.core.database.entity.EventEntity
import com.anshabunin.eventplanner.core.domain.model.NetworkResult
import com.anshabunin.eventplanner.data.remote.model.ResponseWeatherData
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    fun getEvents(): Flow<List<EventEntity>>
    fun getEvent(idEvent: Int): Flow<EventEntity>
    suspend fun updateEvent(event: EventEntity): Int
    suspend fun deleteEvent(idEvent: Int): Int
    suspend fun insertEvent(event: EventEntity): Long
    suspend fun getWeather(city: String): NetworkResult<ResponseWeatherData>?
    suspend fun updateStatusEvent(idEvent: Int, eventStatus: EventStatus): Int
}