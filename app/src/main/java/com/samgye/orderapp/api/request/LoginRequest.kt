package com.samgye.orderapp.api.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("snsType")
    @Expose
    val snsType: String?,

    @SerializedName("snsId")
    @Expose
    val snsId: String?,
    )