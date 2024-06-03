package com.anshabunin.eventplanner.utils

import android.app.TimePickerDialog
import android.content.Context
import java.util.Calendar

class TimePickerHelper(private val context: Context) {
    private var dialog: TimePickerDialog
    private var callback: Callback? = null

    private val listener =
        TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            callback?.onTimeSelected(hourOfDay, minute)
        }

    init {
        val cal = Calendar.getInstance()
        dialog = TimePickerDialog(
            context,
            listener,
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true
        )
    }

    fun showDialog(hourOfDay: Int, minute: Int, callback: Callback?) {
        this.callback = callback
        dialog.updateTime(hourOfDay, minute)
        dialog.show()
    }

    fun showTimePicker(callback: Callback) {
        val cal = Calendar.getInstance()
        val initialHour = cal.get(Calendar.HOUR_OF_DAY)
        val initialMinute = cal.get(Calendar.MINUTE)

        showDialog(initialHour, initialMinute, callback)
    }

    interface Callback {
        fun onTimeSelected(hourOfDay: Int, minute: Int)
    }
}