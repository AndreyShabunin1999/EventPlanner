package com.anshabunin.eventplanner.utils.binding

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("visibility")
fun View.setVisibility(visible: Boolean) {
    if (visible) {
        animate().alpha(1f).translationY(0f)
        visibility = View.VISIBLE
    } else {
        animate().alpha(0f).translationY(1f)
        visibility = View.GONE
    }
}