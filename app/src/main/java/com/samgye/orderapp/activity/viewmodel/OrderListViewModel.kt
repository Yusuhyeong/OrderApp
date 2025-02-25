package com.samgye.orderapp.activity.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.samgye.orderapp.api.ApiClient
import com.samgye.orderapp.data.OrderDetailInfo
import com.samgye.orderapp.data.OrderListInfo
import com.samgye.orderapp.utils.SystemUtil

class OrderListViewModel: ViewModel() {
    private val _order_list_data = MutableLiveData<List<OrderListInfo>>()
    val order_list_data: LiveData<List<OrderListInfo>>
        get() = _order_list_data
    private val _order_detail_info = MutableLiveData<OrderListInfo>()
    val order_detail_info: LiveData<OrderListInfo>
        get() = _order_detail_info

    fun loadOrderList() {
        ApiClient.instance.getOrderList { orderList, error ->
            if (error != null) {
                // Log.e(TAG, "Error fetching order list: ${error.message}")
            } else if (orderList != null) {
                val mappedOrderList = orderList.map { orderResponse ->
                    OrderListInfo(
                        orderType = when (orderResponse.orderType) {
                            "s" -> "매장 식사"
                            "t" -> "포장 주문"
                             else -> "알 수 없음"
                        },
                        orderStat = when (orderResponse.orderStat) {
                            "0" -> "주문 대기"
                            "1" -> "조리 중"
                            "2" -> "주문 취소"
                            "3" -> "조리 완료"
                            "4" -> "주문 완료"
                            else -> "알 수 없음"
                        },
                        usePoint = orderResponse.usePoint,
                        regDttm = SystemUtil.formatToDateOnly(orderResponse.regDttm),
                        menuList = orderResponse.menus.map { menuResponse ->
                            OrderDetailInfo(
                                menuTitle = menuResponse.menuTitle,
                                itemPrice = menuResponse.itemPrice,
                                menuSize = menuResponse.menuSize
                            )
                        }
                    )
                }

                _order_list_data.postValue(mappedOrderList)
            }
        }
    }

    fun clickOrderItem(orderItem: OrderListInfo) {
        _order_detail_info.value = orderItem
    }
}