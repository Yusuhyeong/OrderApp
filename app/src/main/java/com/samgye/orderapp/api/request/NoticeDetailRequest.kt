package com.samgye.orderapp.api.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NoticeDetailRequest(
    @SerializedName("noticeSeq")
    @Expose
    val noticeSeq: Int?
)
