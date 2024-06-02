package com.anshabunin.eventplanner

import android.app.Application
import com.anshabunin.eventplanner.di.AppInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class App : Application(), HasAndroidInjector {

    companion object {
        var instance: App? = null
    }

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
        instance = this

        AppInjector.init(this)

    }

    override fun androidInjector() = dispatchingAndroidInjector
}