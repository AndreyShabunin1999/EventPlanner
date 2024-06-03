package com.anshabunin.eventplanner.di.module

import com.anshabunin.eventplanner.ui.createevent.CreateEventFragment
import com.anshabunin.eventplanner.ui.events.EventsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeEventsFragment(): EventsFragment

    @ContributesAndroidInjector
    abstract fun contributeCreateEventFragment(): CreateEventFragment

}