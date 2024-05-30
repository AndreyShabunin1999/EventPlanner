package com.anshabunin.eventplanner.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.anshabunin.eventplanner.core.database.dao.EventDao
import com.anshabunin.eventplanner.core.database.entity.EventEntity
import com.anshabunin.eventplanner.utils.Converters

@Database(
    entities = [EventEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
}