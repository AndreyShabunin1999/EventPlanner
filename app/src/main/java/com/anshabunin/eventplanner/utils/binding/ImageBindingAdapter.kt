package com.anshabunin.eventplanner.utils.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("loadPhoto")
fun ImageView.setLoadPhoto(url: String?) {
    if (!url.isNullOrEmpty()) {
        this.loadScr(url)
    }
}