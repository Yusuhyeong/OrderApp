package com.samgye.orderapp.api.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class OrderMenuListRequest (
    @SerializedName("menuSeq")
    @Expose
    val menuSeq: Int?,

    @SerializedName("menuSize")
    @Expose
    val menuSize: Int?
)