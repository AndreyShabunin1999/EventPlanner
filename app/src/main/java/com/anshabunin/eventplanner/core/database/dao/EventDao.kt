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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventEntity): Long

    @Delete
    suspend fun deleteEvent(event: EventEntity): Int

    @Update
    suspend fun updateEvent(event: EventEntity): Int
}