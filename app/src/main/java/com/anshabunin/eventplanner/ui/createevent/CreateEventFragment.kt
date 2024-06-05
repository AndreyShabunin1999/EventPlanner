package com.anshabunin.eventplanner.ui.createevent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import com.anshabunin.eventplanner.R
import com.anshabunin.eventplanner.di.Injectable
import com.anshabunin.eventplanner.domain.repository.EventRepository
import android.app.ActionBar.LayoutParams
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.anshabunin.eventplanner.core.database.entity.EventEntity
import com.anshabunin.eventplanner.core.domain.model.ResourceState
import com.anshabunin.eventplanner.databinding.FragmentCreateEventBinding
import com.anshabunin.eventplanner.utils.DatePickerHelper
import com.anshabunin.eventplanner.utils.TimePickerHelper
import kotlinx.coroutines.launch
import javax.inject.Inject

class CreateEventFragment : Fragment(), Injectable {

    private lateinit var binding: FragmentCreateEventBinding
    private lateinit var datePicker: DatePickerHelper
    private lateinit var timePicker: TimePickerHelper

    @Inject
    lateinit var eventRepository: EventRepository

    private val viewModel: CreateEventViewModel by viewModels {
        CreateEventViewModel.factory(eventRepository, requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateEventBinding.inflate(inflater, container, false)

        binding.apply {
            lifecycleOwner = this@CreateEventFragment
            data = viewModel
        }

        binding.backButton.setOnClickListener { findNavController().popBackStack() }

        binding.etEventDate.setOnClickListener { showDatePickerDialog() }
        binding.etEventTime.setOnClickListener { showTimePickerDialog() }

        binding.saveButton.setOnClickListener { insertEvent() }

        datePicker = context?.let { DatePickerHelper(it) }!!
        timePicker = context?.let { TimePickerHelper(it) }!!

        prepare()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.insertEventResult.collect { result ->
                result?.let {
                    val message = when(it) {
                        is ResourceState.SUCCESS -> getString(R.string.success_insert_event)
                        else -> getString(R.string.error_insert_event)
                    }
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    viewModel.clearValue()
                }
            }
        }
    }

    private fun showDatePickerDialog() {
        datePicker.showDatePickerWithAgeLimit(object : DatePickerHelper.Callback {
            override fun onDateSelected(dayOfMonth: Int, month: Int, year: Int) {
                val dayStr = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
                val monthStr = if (month < 10) "0$month" else "$month"
                val dateStr = getString(R.string.date_format, dayStr, monthStr, year)
                binding.etEventDate.setText(dateStr)
                viewModel.eventDate.set(dateStr)
            }
        })
    }

    private fun showTimePickerDialog() {
        timePicker.showTimePicker(object : TimePickerHelper.Callback {
            override fun onTimeSelected(hourOfDay: Int, minute: Int) {
                val timeStr = getString(R.string.time_format, hourOfDay, minute)
                binding.etEventTime.setText(timeStr)
                viewModel.eventTime.set(timeStr)
            }
        })
    }

    private fun insertEvent() {
        val title = viewModel.eventTitle.get()
        val description = viewModel.eventDescription.get()
        val date = viewModel.eventDate.get()
        val time = viewModel.eventTime.get()
        val location = viewModel.eventLocation.get()
        val city = viewModel.eventCity.get()

        if (title.isNullOrEmpty()) {
            showError(getString(R.string.error_title_empty))
            return
        }
        if (description.isNullOrEmpty()) {
            showError(getString(R.string.error_description_empty))
            return
        }
        if (date.isNullOrEmpty()) {
            showError(getString(R.string.error_date_empty))
            return
        }
        if (time.isNullOrEmpty()) {
            showError(getString(R.string.error_time_empty))
            return
        }
        if (location.isNullOrEmpty()) {
            showError(getString(R.string.error_location_empty))
            return
        }
        if (city.isNullOrEmpty()) {
            showError(getString(R.string.error_city_empty))
            return
        }

        val event = EventEntity(
            title = title,
            description = description,
            date = "$date $time",
            location = location,
            city = city
        )
        viewModel.insertEvent(event)
    }

    private fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun prepare() {
        val cityAdapter = context?.let {
            ArrayAdapter(it, R.layout.item_menu_drop_down, viewModel.cityList)
        }
        binding.cityInputEditText.setAdapter(cityAdapter)
        binding.cityInputEditText.doOnTextChanged { _, _, _, _ ->
            if(binding.cityInputEditText.text.length > 3){
                binding.cityInputEditText.dropDownHeight = LayoutParams.WRAP_CONTENT
            } else {
                binding.cityInputEditText.dropDownHeight = 0
            }
        }
        binding.cityInputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val enteredCity = binding.cityInputEditText.text.toString()
                if (enteredCity.isNotEmpty() && !viewModel.cityList.contains(enteredCity)) {
                    binding.cityInputEditText.text = null
                }
            }
        }
    }
}