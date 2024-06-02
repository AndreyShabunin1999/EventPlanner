package com.anshabunin.eventplanner.di

import android.app.Application
import com.anshabunin.eventplanner.App
import com.anshabunin.eventplanner.di.module.AppModule
import com.anshabunin.eventplanner.di.module.DatabaseModule
import com.anshabunin.eventplanner.di.module.FragmentBuildersModule
import com.anshabunin.eventplanner.di.module.MainActivityModule
import com.anshabunin.eventplanner.di.module.NetworkModule
import com.anshabunin.eventplanner.di.module.RepositoryModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@AppScope
@Component(
    modules = [MainActivityModule::class, AndroidInjectionModule::class, FragmentBuildersModule::class,
        RepositoryModule::class, NetworkModule::class, DatabaseModule::class, AppModule::class]
)
interface AppComponent : AndroidInjector<App> {
    override fun inject(app: App)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }
}