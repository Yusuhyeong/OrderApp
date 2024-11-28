package com.samgye.orderapp.activity.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.samgye.orderapp.data.CartMenuInfo

class CartViewModel : ViewModel() {
    private val _cart_menu_list = MutableLiveData<List<CartMenuInfo>>()
    val cart_menu_info: LiveData<List<CartMenuInfo>>
        get() = _cart_menu_list

    private val _is_cart_exist = MutableLiveData<Boolean>()
    val is_cart_exist: LiveData<Boolean>
        get() = _is_cart_exist

    fun loadCartMenu(cartMenuInfo: List<CartMenuInfo>) {
        _cart_menu_list.value = cartMenuInfo
    }

    fun setIsCartExist(isExist: Boolean) {
        _is_cart_exist.value = isExist
    }
}