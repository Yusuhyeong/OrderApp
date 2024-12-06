package com.samgye.orderapp.activity.viewmodel

import androidx.lifecycle.ViewModel
import com.samgye.orderapp.data.PopupData

class PopupViewModel : ViewModel() {
    val popupEvent = SingleLiveEvent<String>()

    val popupData = SingleLiveEvent<PopupData>()

    fun loadPopupData(popupData: PopupData) {
        this.popupData.value = popupData
    }
}