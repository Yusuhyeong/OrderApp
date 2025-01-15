package com.samgye.orderapp.api.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class OrderListResponse<T> (
    @Expose
    val orderSeq: Int,
    @Expose
    val orderType: String,
    @Expose
    val orderStat: String,
    @Expose
    val usePoint: Int,
    @Expose
    val regDttm: String,
    @Expose
    val menus: @RawValue T
    ) : Parcelable