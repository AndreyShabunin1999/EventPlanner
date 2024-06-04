package com.anshabunin.eventplanner.ui.events

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
        getAllEvents()
    }

    private fun getAllEvents() {
        try {
            setLoading(true)
            viewModelScope.launch {
                eventRepository.getEvents().flowOn(Dispatchers.IO).collect { events: List<EventEntity> ->
                    Log.e("ERROR", events.toString())
                    _events.value = events
                }
            }
        } catch (e: Exception) {
            Log.e("ERROR", events.toString())
        } finally {
            setLoading(false)
        }
    }

    private fun setLoading(value: Boolean) {
        _isLoading.postValue(value)
    }

    fun updateSelectedTab(tabIndex: Int) {
        _selectedTab.value = tabIndex
    }

    fun getUpcomingEvents(): List<EventEntity> {
        return _events.value.filter { it.status == EventStatus.UPCOMING }
    }

    fun getAttendedEvents(): List<EventEntity> {
        return _events.value.filter { it.status == EventStatus.ATTENDED }
    }

    fun getMissedEvents(): List<EventEntity> {
        return _events.value.filter { it.status == EventStatus.MISSED }
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