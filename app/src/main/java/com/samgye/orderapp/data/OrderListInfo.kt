package com.samgye.orderapp.data

data class OrderListInfo(
    val orderType: String?,
    val orderStat: String?,
    val usePoint: Int?,
    val regDttm: String?,
    val menuList: List<OrderDetailInfo>?
)
