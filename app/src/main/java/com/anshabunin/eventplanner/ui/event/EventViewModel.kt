package com.anshabunin.eventplanner.ui.event

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.anshabunin.eventplanner.R
import com.anshabunin.eventplanner.core.data.model.EventStatus
import com.anshabunin.eventplanner.core.data.model.getValueStatus
import com.anshabunin.eventplanner.core.database.entity.EventEntity
import com.anshabunin.eventplanner.core.domain.model.onSuccess
import com.anshabunin.eventplanner.data.remote.model.ResponseWeatherData
import com.anshabunin.eventplanner.domain.repository.EventRepository
import com.anshabunin.hotelsapplication.core.domain.model.Resource
import com.anshabunin.hotelsapplication.core.domain.model.ResourceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference

class EventViewModel(
    private val eventRepository: EventRepository,
    private val idEvent: Int,
    context: Context
) : ViewModel() {

    val event = ObservableField<EventEntity>()
    val eventDate = ObservableField<String>()
    val eventTime = ObservableField<String>()
    val eventStatus = ObservableField<String>()

    private val _weatherData = MutableStateFlow<ResponseWeatherData?>(null)
    val weatherData: StateFlow<ResponseWeatherData?> = _weatherData

    private var contextRef = WeakReference(context)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _eventResult = MutableStateFlow<ResourceState?>(null)
    val eventResult: StateFlow<ResourceState?> = _eventResult

    private val _updateEventResult = MutableStateFlow<ResourceState?>(null)
    val updateEventResult: StateFlow<ResourceState?> = _updateEventResult

    init {
        getEvent(idEvent)
    }

    private fun getEvent(idEvent: Int) {
        try {
            setLoading(true)
            viewModelScope.launch {
                eventRepository.getEvent(idEvent).flowOn(Dispatchers.IO).collect { eventEntity: EventEntity ->
                    Log.e("ERRROR", event.toString())
                    event.set(eventEntity)
                    prepareEvent(eventEntity)
                    Log.e("ERRROR", eventEntity.toString())
                    //eventEntity.city?.let { getWeather(it) }
                    //setLoading(false)
                }
            }
        } catch (e: Exception) {
            Log.e("ERRROR", e.message.toString())
        } finally {
            setLoading(false)
        }
    }

    private fun prepareEvent(eventEntity: EventEntity){
        if(eventEntity.date.isNotEmpty() && eventEntity.date != ""){
            eventDate.set(eventEntity.date.split(" ")[0])
            eventTime.set(eventEntity.date.split(" ")[1])
        }
        eventStatus.set(eventEntity.status.getValueStatus(contextRef.get()))
    }

    fun updateEvent(idEvent: Int, eventStatus: EventStatus) {
        try {
            setLoading(true)
            viewModelScope.launch(Dispatchers.IO) {
                val rowsAffected = eventRepository.updateStatusEvent(idEvent, eventStatus)
                if (rowsAffected > 0) {
                    _updateEventResult.value = ResourceState.SUCCESS
                } else {
                    _updateEventResult.value = ResourceState.ERROR
                }
            }
        } catch (e: Exception) {
            _updateEventResult.value = ResourceState.ERROR
        } finally {
            setLoading(false)
        }
    }

    private fun getWeather(city: String): Flow<Resource<ResponseWeatherData>> = flow {
        try {
            val data = withContext(Dispatchers.IO) {
                eventRepository.getWeather(city)
            }
            data?.onSuccess {
                _weatherData.value = it
                Log.e("ERRROR", "USPEH")
                emit(Resource(ResourceState.SUCCESS, it))
            }
        } catch (e: Exception) {
            Log.e("ERRROR", e.message.toString())
            emit(Resource(ResourceState.ERROR))
        } finally {
            Log.e("ERRROR", "HUEVO MENE")
            setLoading(false)
        }
    }

    fun removeEvent(idEvent: Int) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val rowsAffected = eventRepository.deleteEvent(idEvent)
                if (rowsAffected > 0) {
                    _eventResult.value = ResourceState.SUCCESS
                } else {
                    _eventResult.value = ResourceState.ERROR
                }
            }
        } catch (e: Exception) {
            _eventResult.value = ResourceState.ERROR
        }
    }


    private fun setLoading(value: Boolean) {
        _isLoading.postValue(value)
    }

    companion object {
        fun factory(
            eventRepository: EventRepository,
            idEvent: Int,
            context: Context
        ) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return EventViewModel(
                    eventRepository,
                    idEvent,
                    context
                ) as T
            }
        }
    }
}