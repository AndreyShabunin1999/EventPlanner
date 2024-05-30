package com.anshabunin.eventplanner.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.anshabunin.eventplanner.core.database.entity.EventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Query("SELECT * FROM event")
    fun getListFlow(): Flow<List<EventEntity>?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEvent(event: EventEntity)

    @Query("DELETE FROM event WHERE id = :eventId")
    fun deleteEventById(eventId: Int)

    @Update
    fun updateEvent(event: EventEntity)
}