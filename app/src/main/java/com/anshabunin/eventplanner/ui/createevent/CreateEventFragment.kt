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
import com.anshabunin.eventplanner.databinding.FragmentCreateEventBinding
import com.anshabunin.eventplanner.di.Injectable
import com.anshabunin.eventplanner.domain.repository.EventRepository
import android.app.ActionBar.LayoutParams
import androidx.navigation.fragment.findNavController
import com.anshabunin.eventplanner.utils.DatePickerHelper
import com.anshabunin.eventplanner.utils.TimePickerHelper
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

        binding.backButton.setOnClickListener { findNavController().popBackStack() }

        binding.etEventDate.setOnClickListener { showDatePickerDialog() }
        binding.etEventTime.setOnClickListener { showTimePickerDialog() }

        datePicker = context?.let { DatePickerHelper(it) }!!
        timePicker = context?.let { TimePickerHelper(it) }!!

        prepare()
        return binding.root
    }

    private fun showDatePickerDialog() {
        datePicker.showDatePickerWithAgeLimit(object : DatePickerHelper.Callback {
            override fun onDateSelected(dayOfMonth: Int, month: Int, year: Int) {
                val dayStr = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
                val monthStr = if (month < 10) "0$month" else "$month"
                binding.etEventDate.setText("$dayStr.$monthStr.$year")
                viewModel.eventDate.set("$dayStr.$monthStr.$year")
            }
        })
    }

    private fun showTimePickerDialog() {
        timePicker.showTimePicker(object : TimePickerHelper.Callback {
            override fun onTimeSelected(hourOfDay: Int, minute: Int) {
                binding.etEventTime.setText("$hourOfDay:$minute")
                viewModel.eventTime.set("$hourOfDay:$minute")
            }
        })
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