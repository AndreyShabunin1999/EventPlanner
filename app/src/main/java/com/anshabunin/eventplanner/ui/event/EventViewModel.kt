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
import com.anshabunin.eventplanner.core.database.entity.EventEntity
import com.anshabunin.eventplanner.domain.repository.EventRepository
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

    private var contextRef = WeakReference(context)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _eventResult = MutableStateFlow<String?>(null)
    val eventResult: StateFlow<String?> = _eventResult

    init {
        getEvent(idEvent)
    }

    private fun getEvent(idEvent: Int) {
        try {
            setLoading(true)
            viewModelScope.launch {
                eventRepository.getEvent(idEvent).flowOn(Dispatchers.IO).collect { eventEntity: EventEntity ->
                    Log.e("ERROR", event.toString())
                    event.set(eventEntity)
                    if(!eventEntity.date.isNullOrEmpty() || eventEntity.date == ""){
                        eventDate.set(eventEntity.date.split(" ")[0])
                        eventTime.set(eventEntity.date.split(" ")[1])
                    }
                    setLoading(false)
                }
            }
        } catch (e: Exception) {
            Log.e("ERROR", e.message.toString())
            setLoading(false)
        }
    }

    fun removeEvent(idEvent: Int) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val rowsAffected = eventRepository.deleteEvent(idEvent)
                if (rowsAffected > 0) {
                    _eventResult.value = contextRef.get()
                        ?.let { ContextCompat.getString(it, R.string.success_remove_event) }
                } else {
                    _eventResult.value = contextRef.get()
                        ?.let { ContextCompat.getString(it, R.string.error_remove_event) }
                }
            }
        } catch (e: Exception) {
            _eventResult.value = contextRef.get()
                ?.let { ContextCompat.getString(it, R.string.error_remove_event) }
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