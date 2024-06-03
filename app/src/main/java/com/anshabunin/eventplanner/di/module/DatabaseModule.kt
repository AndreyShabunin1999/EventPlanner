package com.anshabunin.eventplanner.di.module

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.anshabunin.eventplanner.core.database.AppDatabase
import com.anshabunin.eventplanner.di.AppScope
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule {

    @Provides
    @AppScope
    fun getDatabase(context: Context): AppDatabase {
        return Room
            .databaseBuilder(context, AppDatabase::class.java, "EventPlannerBase")
            .fallbackToDestructiveMigration()
            .build()
    }
}