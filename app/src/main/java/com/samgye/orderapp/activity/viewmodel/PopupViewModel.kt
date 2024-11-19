package com.samgye.orderapp.activity.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.samgye.orderapp.data.PopupData

class PopupViewModel : ViewModel() {
    val popupEvent = SingleLiveEvent<String>()

    private val _popup_data = MutableLiveData<PopupData>()
    val popup_data: LiveData<PopupData>
        get() = _popup_data

    fun loadPopupData(popupData: PopupData) {
        _popup_data.value = popupData
    }
}