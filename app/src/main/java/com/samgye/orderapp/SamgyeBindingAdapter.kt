package com.samgye.orderapp

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter

val Tag = "SamgyeBindingAdapter"

@BindingAdapter("app:menu_click")
fun menuClick(constraintLayout: ConstraintLayout, visibility: Boolean) {
    constraintLayout.setOnClickListener {
        if (visibility) {
            constraintLayout.visibility = View.VISIBLE
        } else {
            constraintLayout.visibility = View.GONE
        }
    }
}