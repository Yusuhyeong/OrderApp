package com.samgye.orderapp.api.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

@Parcelize
class ResponseMenuList(
    @Expose
    val menuSeq: Int?,
    @Expose
    val menuTitle: String?,
    @Expose
    val menuInfo: String?,
    @Expose
    val menuImgUrl: String?,
    @Expose
    val menuPrice: String?,
    @Expose
    val popularYn: String?
) : Parcelable