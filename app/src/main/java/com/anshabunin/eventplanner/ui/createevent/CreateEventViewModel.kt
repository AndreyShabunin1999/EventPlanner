package com.anshabunin.eventplanner.ui.createevent

import android.content.Context
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.anshabunin.eventplanner.core.database.entity.EventEntity
import com.anshabunin.eventplanner.core.domain.model.ResourceState
import com.anshabunin.eventplanner.domain.repository.EventRepository
import com.anshabunin.eventplanner.utils.mapToList
import com.anshabunin.eventplanner.utils.readJSONFromAssets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

class CreateEventViewModel(
    private val eventRepository: EventRepository,
    context: Context
) : ViewModel() {

    private var contextRef = WeakReference(context)
    val cityList = mutableListOf<String>()

    val eventTitle = ObservableField<String>()
    val eventDate = ObservableField<String>()
    val eventTime = ObservableField<String>()
    val eventLocation = ObservableField<String>()
    val eventDescription = ObservableField<String>()
    val eventCity = ObservableField<String>()


    init {
        contextRef.get()?.let {
            cityList.addAll(mapToList(readJSONFromAssets(it, "cities_ru_short.json")))
        }
    }

    private val _insertEventResult = MutableStateFlow<ResourceState?>(null)
    val insertEventResult: StateFlow<ResourceState?> = _insertEventResult

    fun insertEvent(event: EventEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val insertedRowId = eventRepository.insertEvent(event)
                if (insertedRowId != -1L) {
                    _insertEventResult.value = ResourceState.SUCCESS
                } else {
                    _insertEventResult.value = ResourceState.ERROR
                }
            } catch (e: Exception) {
                _insertEventResult.value = ResourceState.ERROR
            }
        }
    }

    fun clearValue(){
        _insertEventResult.value = null
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