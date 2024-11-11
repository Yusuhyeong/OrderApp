package com.samgye.orderapp.api.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class BaseResponse<T>(
    @Expose
    val code: Int,
    @Expose
    val msg: String,
    @Expose
    val dmsg: String,
    @Expose
    val data: @RawValue T?
): Parcelable
