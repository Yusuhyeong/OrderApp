package com.samgye.orderapp.api.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

@Parcelize
data class NoticeDetailResponse(
    @Expose
    val noticeSeq: Int?,
    @Expose
    val noticeTitle: String?,
    @Expose
    val noticeCont: String?,
    @Expose
    val regDttm: String?,
    @Expose
    val regrNm: String?,
    @Expose
    val noticeImg: String?
    ) : Parcelable
