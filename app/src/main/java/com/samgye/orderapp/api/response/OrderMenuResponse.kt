package com.samgye.orderapp.api.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderMenuResponse (
    @Expose
    val menuSeq: Int,
    @Expose
    val menuTitle: String,
    @Expose
    val itemPrice: String,
    @Expose
    val menuSize: Int,
) : Parcelable