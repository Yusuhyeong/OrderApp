package com.samgye.orderapp.data

data class OrderInfo (
    val orderType: String?,
    val point: Int?,
    val chooseList: List<OrderListInfo>
)