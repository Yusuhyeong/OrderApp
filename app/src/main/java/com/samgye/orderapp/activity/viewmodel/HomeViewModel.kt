package com.samgye.orderapp.activity.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
    private val _selected_id = MutableLiveData<String>()
    val selected_id: LiveData<String>
        get() = _selected_id
    private val _is_menu_visible = MutableLiveData<Boolean>(false)
    val is_menu_visible: LiveData<Boolean>
        get() = _is_menu_visible
    fun menuClick(view: View) {
        _selected_id.value = view.id.toString()
        _is_menu_visible.value = _is_menu_visible.value?.not() ?: false
    }

    fun menuCategoryClick(view: View) {
        _selected_id.value = view.id.toString()
    }
}