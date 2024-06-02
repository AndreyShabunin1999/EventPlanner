package com.anshabunin.eventplanner.ui.events

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anshabunin.eventplanner.core.data.model.EventStatus
import com.anshabunin.eventplanner.core.database.entity.EventEntity
import com.anshabunin.eventplanner.domain.repository.EventRepository
import com.anshabunin.hotelsapplication.core.domain.model.Resource
import com.anshabunin.hotelsapplication.core.domain.model.ResourceState
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class EventsViewModel(
    private val eventRepository: EventRepository,
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
        var event = EventEntity(1, "ДР", "Туса у Олега", "22.08.2024", "Фрунзе 3, кв 38","Санкт-Петербург", "", EventStatus.ATTENDED)
        insertEvent(event)
        getEvents()
    }

    private fun getEvents(): Flow<List<EventEntity>?> = flow {
        try {
            setLoading(true)
            val dataFlow = withContext(Dispatchers.IO) {
                eventRepository.getEvents()
            }
            dataFlow.collect { data ->
                if (!data.isNullOrEmpty()) {
                    _events.value = data
                    Log.e("ERRROR", data.toString())
                }
            }
        } catch (e: Exception) {
        } finally {
            setLoading(false)
        }
    }


    fun update(event: EventEntity): Flow<Resource<Unit>> = flow {
        try {
            val rowsAffected = withContext(Dispatchers.IO) {
                eventRepository.updateEvent(event)
            }
            if (rowsAffected > 0) {
                //emit(Resource.success(Unit))
            } else {
                //emit(Resource.error("Failed to update event", null))
            }
        } catch (e: Exception) {
            //emit(Resource.error("An error occurred", null))
        }
    }

    fun deleteEvent(idEvent: Int): Flow<Resource<Unit>> = flow {
        try {
            val rowsAffected = withContext(Dispatchers.IO) {
                eventRepository.deleteEvent(idEvent)
            }
            if (rowsAffected > 0) {
                //emit(Resource.success(Unit))
            } else {
                //emit(Resource.error("Failed to update event", null))
            }
        } catch (e: Exception) {
            //emit(Resource(ResourceState.ERROR))
        } finally {
            //emit(Resource.error("An error occurred", null))
        }
    }

    fun insertEvent(event: EventEntity): Flow<Resource<Unit>> = flow {
        try {
            val insertedRowId = withContext(Dispatchers.IO) {
                eventRepository.insertEvent(event)
            }
            if (insertedRowId != -1L) {
                //emit(Resource.success(Unit))
            } else {
                //emit(Resource.error("Failed to insert event", null))
            }
        } catch (e: Exception) {
            //emit(Resource.error("An error occurred", null))
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