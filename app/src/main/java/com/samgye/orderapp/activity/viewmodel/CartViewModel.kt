package com.samgye.orderapp.activity.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.samgye.orderapp.data.CartMenuInfo

class CartViewModel : ViewModel() {
    private val _cart_menu_list = MutableLiveData<List<CartMenuInfo>>()
    val cart_menu_list: LiveData<List<CartMenuInfo>>
        get() = _cart_menu_list

    private val _order_type = MutableLiveData<String>()
    val order_type: LiveData<String>
        get() = _order_type

    private val _is_cart_exist = MutableLiveData<Boolean>()
    val is_cart_exist: LiveData<Boolean>
        get() = _is_cart_exist

    private val _use_point = MutableLiveData<Int>()
    val use_point: LiveData<Int>
        get() = _use_point

    private val _total_price = MutableLiveData<Int>()
    val total_price: LiveData<Int>
        get() = _total_price

    private val _puchase_price = MutableLiveData<Int>()
    val puchase_price: LiveData<Int>
        get() = _puchase_price


    fun loadCartMenu(cartMenuList: List<CartMenuInfo>) {
        _cart_menu_list.value = cartMenuList
    }

    fun setIsCartExist(isExist: Boolean) {
        _is_cart_exist.value = isExist
    }

    fun setOrderType(orderType: Boolean) {
        if (orderType) {
            _order_type.value = "매장 식사"
        } else {
            _order_type.value = "포장 주문"
        }
    }

    fun updateCartMenu(cartMenuInfo: CartMenuInfo) {
        Log.d("TEST_LOG", "updateCartMenu")
        val cartMenuList = cart_menu_list.value?.toMutableList()
        if (cartMenuInfo.menuSize == 0) {
            Log.d("TEST_LOG", "menuInfo size 0")
            cartMenuList?.removeIf {
                Log.d("TEST_LOG", "cartMenuList remove, ${cartMenuInfo.menuSeq}")
                it.menuSeq == cartMenuInfo.menuSeq
            }
        } else {
            Log.d("TEST_LOG", "menuInfo size 0 over")
            for(i: Int in cartMenuList?.indices!!) {
                if (cartMenuList[i].menuSeq == cartMenuInfo.menuSeq) {
                    cartMenuList[i] = cartMenuInfo
                    _cart_menu_list.value = cartMenuList
                    return
                }
            }
        }

        _cart_menu_list.value = cartMenuList
    }

    fun setUsePoint(point: String) {
        _use_point.value = point.toInt()
        setPurchasePrice()
    }

    fun setTotalPrice() {
        val cartMenuList = cart_menu_list.value?.toMutableList()
        var totalPrice = 0
        for (i: Int in cartMenuList?.indices!!) {
            val menuPrice = cartMenuList[i].menuPrice?.times(cartMenuList[i].menuSize!!) ?: 0
            totalPrice += menuPrice
        }
        _total_price.value = totalPrice
        setPurchasePrice()
    }

    fun setPurchasePrice() {
        val totalPrice = _total_price.value
        val point = _use_point.value

        if (point != null) {
            _puchase_price.value = totalPrice?.minus(point)
        } else {
            _puchase_price.value = totalPrice
        }
    }
}