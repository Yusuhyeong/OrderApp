package com.samgye.orderapp.activity.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
    private val _selected_id = MutableLiveData<String>()
    val selected_id: LiveData<String>
        get() = _selected_id
    private val _isMenuVisible = MutableLiveData<Boolean>(false)
    val isMenuVisible: LiveData<Boolean>
        get() = _isMenuVisible
    fun menuClick(view: View) {
        _selected_id.value = view.id.toString()
    }
    private fun menuVisible() {
        _isMenuVisible.value = !(_isMenuVisible.value ?: false)
    }
}