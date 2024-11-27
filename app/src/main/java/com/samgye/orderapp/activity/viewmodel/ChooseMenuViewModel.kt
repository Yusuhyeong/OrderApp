package com.samgye.orderapp.activity.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.samgye.orderapp.data.ChooseListInfo

class ChooseMenuViewModel : ViewModel() {
    private val _menu_title = MutableLiveData<String>()
    val menu_title: LiveData<String>
        get() = _menu_title

    private val _menu_info = MutableLiveData<String>()
    val menu_info: LiveData<String>
        get() = _menu_info

    private val _menu_seq = MutableLiveData<Int>()
    val menu_seq: LiveData<Int>
        get() = _menu_seq

    private val _menu_size = MutableLiveData<Int>()
    val menu_size: LiveData<Int>
        get() = _menu_size

    private val _menu_img_url = MutableLiveData<String>()
    val menu_img_url: LiveData<String>
        get() = _menu_img_url

    private val _menu_price = MutableLiveData<Int>()
    val menu_price: LiveData<Int>
        get() = _menu_price


    fun loadChooseListData(menuTitle: String, menuInfo: String, menuSeq: Int, menuSize: Int, menuImgUrl: String, menuPrice: Int) {
        _menu_info.value = menuInfo
        _menu_title.value = menuTitle
        _menu_seq.value = menuSeq
        _menu_size.value = menuSize
        _menu_img_url.value = menuImgUrl
        _menu_price.value = menuPrice
    }

    fun plusSize() {
        val size = _menu_size.value ?: 0
        _menu_size.value = size + 1
    }

    fun minusSize() {
        val size = _menu_size.value ?: 0
        if (size > 0) {
            _menu_size.value = size - 1
        }
    }
}