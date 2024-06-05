package com.anshabunin.eventplanner.ui.editevent

import android.content.Context
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.anshabunin.eventplanner.core.data.model.EventStatus
import com.anshabunin.eventplanner.core.database.entity.EventEntity
import com.anshabunin.eventplanner.core.domain.model.ResourceState
import com.anshabunin.eventplanner.domain.repository.EventRepository
import com.anshabunin.eventplanner.utils.mapToList
import com.anshabunin.eventplanner.utils.readJSONFromAssets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

class EditEventViewModel(
    private val eventRepository: EventRepository,
    private val idEvent: Int,
    context: Context
) : ViewModel()  {

    private var contextRef = WeakReference(context)
    val cityList = mutableListOf<String>()
    val eventTitle = ObservableField<String>()
    val eventDate = ObservableField<String>()
    val eventTime = ObservableField<String>()
    val eventLocation = ObservableField<String>()
    val eventDescription = ObservableField<String>()
    val eventStatus = ObservableField<EventStatus>()
    val eventCity = ObservableField<String>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _updateEventResult = MutableStateFlow<ResourceState?>(null)
    val updateEventResult: StateFlow<ResourceState?> = _updateEventResult

    init {
        contextRef.get()?.let {
            cityList.addAll(mapToList(readJSONFromAssets(it, "cities_ru_short.json")))
        }
    }

    init {
        getEvent(idEvent)
    }

    private fun getEvent(idEvent: Int) {
        try {
            setLoading(true)
            viewModelScope.launch {
                eventRepository.getEvent(idEvent).flowOn(Dispatchers.IO).collect { eventEntity: EventEntity ->
                    prepareEvent(eventEntity)
                }
            }
        } catch (e: Exception) {
            Log.e("ERRROR", e.message.toString())
        } finally {
            setLoading(false)
        }
    }

    private fun prepareEvent(event: EventEntity){
        eventCity.set(event.city)
        eventTitle.set(event.title)
        if(event.date.isNotEmpty() && event.date != ""){
            eventDate.set(event.date.split(" ")[0])
            eventTime.set(event.date.split(" ")[1])
        }
        eventLocation.set(event.location)
        eventDescription.set(event.description)
        eventStatus.set(event.status)
    }

    private fun setLoading(value: Boolean) {
        _isLoading.postValue(value)
    }

    fun createAndUpdateEvent(
        title: String,
        description: String,
        dateTime: String,
        location: String,
        city: String,
        status: EventStatus
    ) {
        val event = EventEntity(
            id = idEvent,
            title = title,
            description = description,
            date = dateTime,
            location = location,
            city = city,
            status = status
        )
        updateEvent(event)
    }


    fun updateEvent(event: EventEntity) {
        try {
            setLoading(true)
            viewModelScope.launch(Dispatchers.IO) {
                val rowsAffected = eventRepository.updateEvent(event)
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

    fun clearValue(){
        _updateEventResult.value = null
    }

    companion object {
        fun factory(
            eventRepository: EventRepository,
            idEvent: Int,
            context: Context
        ) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return EditEventViewModel(
                    eventRepository,
                    idEvent,
                    context
                ) as T
            }
        }
    }
}