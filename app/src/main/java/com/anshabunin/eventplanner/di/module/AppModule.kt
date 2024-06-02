package com.anshabunin.eventplanner.di.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @Provides
    fun getApplicationContext(application: Application): Context {
        return application
    }
}