package com.samgye.orderapp.api.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserInfoResponse(
    @Expose
    val username: String,
    @Expose
    val snsType: String
    ) : Parcelable