package com.samgye.orderapp.api.request

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class LoginRequest(
    @SerializedName("snsType")
    @Expose
    val snsType: String?,

    @SerializedName("snsId")
    @Expose
    val snsId: String?,
    )