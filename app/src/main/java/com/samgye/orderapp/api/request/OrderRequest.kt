package com.samgye.orderapp.api.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class OrderRequest(
    @SerializedName("orderType")
    @Expose
    val orderType: String?,

    @SerializedName("usePoint")
    @Expose
    val usePoint: Int?,

    @SerializedName("menuList")
    @Expose
    val menuList: List<OrderMenuListRequest>?
)
