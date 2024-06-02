package com.anshabunin.eventplanner.di.module

import com.anshabunin.eventplanner.data.remote.EventService
import com.anshabunin.eventplanner.di.AppScope
import com.anshabunin.eventplanner.utils.BASE_URL
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.Protocol
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.Collections


@Module
class NetworkModule {

    @Provides
    @AppScope
    fun providesGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }

    @Provides
    @AppScope
    fun providesOkhttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .protocols(Collections.singletonList(Protocol.HTTP_1_1))
            .build()
    }

    @Provides
    @AppScope
    fun providesUserService(okHttpClient: OkHttpClient, gson: Gson): EventService {
        gson.serializeNulls()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
            .create(EventService::class.java)
    }
}