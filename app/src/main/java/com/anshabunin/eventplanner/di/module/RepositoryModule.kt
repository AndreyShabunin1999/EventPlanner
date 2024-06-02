package com.anshabunin.eventplanner.di.module

import com.anshabunin.eventplanner.core.database.AppDatabase
import com.anshabunin.eventplanner.data.remote.EventService
import com.anshabunin.eventplanner.di.AppScope
import com.anshabunin.eventplanner.domain.repository.EventRepository
import com.anshabunin.eventplanner.domain.repository.impl.EventRepositoryImpl
import dagger.Module
import dagger.Provides

@Module(includes = [NetworkModule::class])
class RepositoryModule {
    @Provides
    @AppScope
    fun providesEventRepository(eventService: EventService, appDatabase: AppDatabase): EventRepository {
        return EventRepositoryImpl(eventService, appDatabase)
    }
}