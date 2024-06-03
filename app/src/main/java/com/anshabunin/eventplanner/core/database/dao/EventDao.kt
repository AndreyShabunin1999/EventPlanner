package com.anshabunin.eventplanner.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.anshabunin.eventplanner.core.database.entity.EventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Query("SELECT * FROM event")
    fun getEventsList(): Flow<List<EventEntity>>

    @Query("SELECT * FROM event WHERE id = :idEvent")
    fun getEvent(idEvent: Int): Flow<EventEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventEntity): Long

    @Query("DELETE FROM event WHERE id = :idEvent")
    suspend fun deleteEvent(idEvent: Int): Int

    @Update
    suspend fun updateEvent(event: EventEntity): Int
}