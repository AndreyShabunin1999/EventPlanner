package com.anshabunin.eventplanner.ui.event

import android.content.Context
import android.util.Log
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
import com.anshabunin.eventplanner.core.domain.model.NetworkResult
import com.anshabunin.eventplanner.core.domain.model.ResourceState
import com.anshabunin.eventplanner.data.remote.model.ResponseWeatherData
import com.anshabunin.eventplanner.domain.repository.EventRepository
import com.anshabunin.eventplanner.utils.WEATHER_IMAGE
import com.anshabunin.eventplanner.utils.convertDateFormat
import com.anshabunin.eventplanner.utils.findNearestTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
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

    val weatherImage = ObservableField<String>()
    val minTemp = ObservableField<String>()
    val maxTemp = ObservableField<String>()

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
                eventRepository.getEvent(idEvent).flowOn(Dispatchers.IO)
                    .collect { eventEntity: EventEntity? ->
                        eventEntity?.let {
                            event.set(it)
                            prepareEvent(it)
                            getWeather(it.city)
                        }
                    }
            }
        } catch (e: Exception) {
            Log.e("ERRROR", e.message.toString())
        } finally {
            setLoading(false)
        }
    }

    private fun prepareWeather(weatherData: ResponseWeatherData){
        val findData = eventDate.get()?.let { convertDateFormat(it) }
        val nearestTime = eventTime.get()?.let { findNearestTime(it) }

        val targetWeather = weatherData.list.find {
            val fromListData = it.dtTxt?.split(" ")?.get(0)
            val fromListTime = it.dtTxt?.split(" ")?.get(1)
            (findData == fromListData) && (fromListTime == nearestTime)
        }

        targetWeather?.let {
            weatherImage.set(contextRef.get()?.resources?.getString(R.string.image_format, WEATHER_IMAGE, it.weather[0].icon))
            maxTemp.set(it.main?.tempMax?.let { tempMax -> kelvinToCelsius(tempMax).toString() })
            minTemp.set(it.main?.tempMin?.let { tempMin -> kelvinToCelsius(tempMin).toString() })
        }
    }

    private fun kelvinToCelsius(kelvin: Double): Int = (kelvin - 273.15).toInt()

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

    private fun getWeather(city: String) {
        viewModelScope.launch {
            try {
                setLoading(true)
                val result = eventRepository.getWeather(city)
                when (result) {
                    is NetworkResult.Success -> {
                        _weatherData.value = result.data
                        weatherData.value?.let { prepareWeather(it) }
                    }
                    is NetworkResult.ErrorServer -> {
                        Log.e("Weather", "Error: ${result.msg}")
                    }
                    is NetworkResult.Exception -> {
                        Log.e("Weather", "Exception: ${result.e}")
                    } else -> {
                        Log.e("Weather", "Error")
                    }
                }
            } catch (e: Exception) {
                Log.e("Weather", "Exception: ${e.message}")
            } finally {
                setLoading(false)
            }
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

    fun clearUpdateResult(){
        _updateEventResult.value = null
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