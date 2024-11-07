package com.samgye.orderapp.api.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

@Parcelize
data class UsernameResponse(
    @Expose
    val code: Int,
    @Expose
    val msg: String,
    @Expose
    val data: Int
): Parcelable