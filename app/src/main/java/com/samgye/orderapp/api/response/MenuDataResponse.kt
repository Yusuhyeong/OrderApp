package com.samgye.orderapp.api.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
class MenuDataResponse<T>(
    @Expose
    val categorySeq: Int?,
    @Expose
    val categoryNm: String?,
    @Expose
    val menu: @RawValue T?
) : Parcelable