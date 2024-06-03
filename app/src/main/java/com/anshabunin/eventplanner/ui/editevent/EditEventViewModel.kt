package com.anshabunin.eventplanner.ui.editevent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anshabunin.eventplanner.domain.repository.EventRepository
import com.anshabunin.eventplanner.ui.events.EventsViewModel

class EditEventViewModel(
    private val eventRepository: EventRepository,
    private val idEvent: Int
) : ViewModel()  {


    companion object {
        fun factory(
            eventRepository: EventRepository,
            idEvent: Int
        ) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return EditEventViewModel(
                    eventRepository,
                    idEvent
                ) as T
            }
        }
    }
}