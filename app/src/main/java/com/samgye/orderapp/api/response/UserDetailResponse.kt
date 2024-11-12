package com.samgye.orderapp.api.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserDetailResponse(
    @Expose
    val point: Int,
    @Expose
    val email: String
) : Parcelable
