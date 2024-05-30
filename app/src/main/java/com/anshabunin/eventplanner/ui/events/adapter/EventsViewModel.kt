package com.anshabunin.eventplanner.ui.events.adapter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anshabunin.eventplanner.domain.repository.EventRepository

class EventsViewModel(
    private val eventRepository: EventRepository,
) : ViewModel() {

    companion object {
        fun factory(
            eventRepository: EventRepository,
        ) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return EventsViewModel(
                    eventRepository
                ) as T
            }
        }
    }
}