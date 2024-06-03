package com.anshabunin.eventplanner.utils

import android.app.DatePickerDialog
import android.content.Context
import java.util.*

class DatePickerHelper(private val context: Context) {
    private var dialog: DatePickerDialog
    private var callback: Callback? = null
    private val listener =
        DatePickerDialog.OnDateSetListener { datePicker, year, monthOfYear, dayOfMonth ->
            val month = if (year == Calendar.getInstance().get(Calendar.YEAR)) monthOfYear
            else monthOfYear + 1
            callback?.onDateSelected(dayOfMonth, month, year)
        }

    init {
        val cal = Calendar.getInstance()
        dialog = DatePickerDialog(
            context,
            listener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )
        setMinDate(cal.time.time)
    }

    fun showDialog(dayOfMonth: Int, month: Int, year: Int, callback: Callback?) {
        this.callback = callback
        dialog.datePicker.init(year, month, dayOfMonth, null)
        dialog.show()
    }

    fun showDatePickerWithAgeLimit(callback: Callback) {
        val cal = Calendar.getInstance()
        val initialDay = cal.get(Calendar.DAY_OF_MONTH)
        val initialMonth = cal.get(Calendar.MONTH)
        val initialYear = cal.get(Calendar.YEAR)

        showDialog(initialDay, initialMonth, initialYear, callback)
    }


    fun setMinDate(minDate: Long) {
        dialog.datePicker.minDate = minDate
    }

    fun setMaxDate(maxDate: Long) {
        dialog.datePicker.maxDate = maxDate
    }

    fun setMaxAge(age: Int) {
        val cal = Calendar.getInstance()
        cal.add(Calendar.YEAR, -age)
        setMaxDate(cal.time.time)
    }

    interface Callback {
        fun onDateSelected(dayOfMonth: Int, month: Int, year: Int)
    }
}


