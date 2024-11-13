package com.samgye.orderapp.api.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

@Parcelize
data class NoticeInfoResponse(
    @Expose
    val noticeSeq: Int,
    @Expose
    val noticeTitle: String
) : Parcelable

