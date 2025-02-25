package com.samgye.orderapp.activity.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AlertViewModel : ViewModel() {
    private val TAG = this.javaClass.simpleName

    private val _str_title = MutableLiveData<String>()
    val str_title = _str_title

    private val _str_message = MutableLiveData<String>()
    val str_message = _str_message

    private val _str_confirm = MutableLiveData<String>()
    val str_confirm = _str_confirm

    private val _str_cancel = MutableLiveData<String>()
    val str_cancel = _str_cancel

    private val _is_one_btn = MutableLiveData<Boolean>()
    val is_one_btn = _is_one_btn

    val confirm_event: SingleLiveEvent<Boolean> = SingleLiveEvent<Boolean>()

    val cancel_event: SingleLiveEvent<Boolean> = SingleLiveEvent<Boolean>()

    fun setTitleText(text: String) {
        _str_title.value = text
    }
    fun setMessageText(text: String) {
        _str_message.value = text
    }
    fun setConfirmText(text: String) {
        _str_confirm.value = text
    }
    fun setCancelText(text: String) {
        _str_cancel.value = text
    }

    fun setIsOneBtn(isVisible: Boolean) {
        _is_one_btn.value = isVisible
    }

    fun onConfirmClick() {
        confirm_event.setValue(true)
    }

    fun onCancelClick() {
        cancel_event.setValue(true)
    }
}