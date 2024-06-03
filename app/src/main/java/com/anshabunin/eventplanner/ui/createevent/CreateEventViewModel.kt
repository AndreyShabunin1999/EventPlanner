package com.anshabunin.eventplanner.ui.createevent

import android.content.Context
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anshabunin.eventplanner.domain.repository.EventRepository
import com.anshabunin.eventplanner.ui.events.EventsViewModel
import com.anshabunin.eventplanner.utils.mapToList
import com.anshabunin.eventplanner.utils.readJSONFromAssets
import java.lang.ref.WeakReference

class CreateEventViewModel(
    private val eventRepository: EventRepository,
    context: Context
) : ViewModel() {

    private var contextRef = WeakReference(context)
    val cityList = mutableListOf<String>()

    val eventDate = ObservableField<String>()
    val eventTime = ObservableField<String>()

    init {
        contextRef.get()?.let {
            cityList.addAll(mapToList(readJSONFromAssets(it, "cities_ru_short.json")))
        }
    }


    companion object {
        fun factory(
            eventRepository: EventRepository,
            context: Context
        ) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return CreateEventViewModel(
                    eventRepository,
                    context
                ) as T
            }
        }
    }
}