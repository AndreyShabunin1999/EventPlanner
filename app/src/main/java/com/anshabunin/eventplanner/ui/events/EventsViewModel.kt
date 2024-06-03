package com.anshabunin.eventplanner.ui.events

import android.content.Context
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.anshabunin.eventplanner.core.data.model.EventStatus
import com.anshabunin.eventplanner.core.database.entity.EventEntity
import com.anshabunin.eventplanner.domain.repository.EventRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EventsViewModel(
    private val eventRepository: EventRepository
) : ViewModel() {

    private val _events = MutableStateFlow<List<EventEntity>>(emptyList())
    val events: StateFlow<List<EventEntity>> get() = _events

    val visibilityEmptyView = ObservableBoolean(false)

    private val _selectedTab = MutableLiveData(0)

    val selectedTab: LiveData<Int>
        get() = _selectedTab

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    init {

        var event = EventEntity(
            id = 4,
            title =  "1 сентября",
            description = "Туса у Андрея",
            date = "22.08.2026",
            location = "Фрунзе 4, кв 39",
            city = "Москва",
            imageUrl = "gg",
            status = EventStatus.ATTENDED)

        //updateEvent(event)

        //removeEvent(event)

        //insertEvent(event)

        //getEvents()
    }

    fun getEvents() {
        try {
            viewModelScope.launch {
                eventRepository.getEvents().flowOn(Dispatchers.IO).collect { events: List<EventEntity> ->
                    Log.e("ERROR", events.toString())
                    _events.value = events
                }
            }
        } catch (e: Exception) {
            Log.e("ERROR", events.toString())
        }

    }

    fun insertEvent(event: EventEntity) {
        try{
            viewModelScope.launch(Dispatchers.IO) {
                val insertedRowId = eventRepository.insertEvent(event)
                if (insertedRowId != -1L) {
                    Log.e("SUCCESS", "Event inserted with ID: $insertedRowId")
                } else {
                    Log.e("ERROR", "Failed to insert event")
                }
            }
        } catch (e: Exception) {
            Log.e("ERROR", events.toString())
        }
    }

    fun removeEvent(event: EventEntity) {
        try{
            viewModelScope.launch(Dispatchers.IO) {
                val rowsAffected = eventRepository.deleteEvent(event)
                if (rowsAffected > 0) {
                    Log.e("SUCCESS", "Event deleted")
                } else {
                    Log.e("ERROR", "Failed to delete event")
                }
            }
        } catch (e: Exception) {
            Log.e("ERROR", events.toString())
        }
    }

    fun updateEvent(event: EventEntity) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val rowsAffected = eventRepository.updateEvent(event)
                if (rowsAffected > 0) {
                    Log.e("SUCCESS", "Event updated")
                } else {
                    Log.e("ERROR", "Failed to update event")
                }
            }
        } catch (e: Exception) {
            Log.e("ERROR", events.toString())
        }
    }

    private fun setLoading(value: Boolean) {
        _isLoading.postValue(value)
    }

    fun updateSelectedTab(tabIndex: Int) {
        _selectedTab.value = tabIndex
    }

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