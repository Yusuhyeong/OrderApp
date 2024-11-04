package com.samgye.orderapp.api.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginResult(
    @SerializedName("tokenType")
    @Expose
    val tokenType: String?,

    @SerializedName("accessToken")
    @Expose
    val accessToken: String?,

    @SerializedName("refreshToken")
    @Expose
    val refreshToken: String?,

    @SerializedName("expiresIn")
    @Expose
    val expiresIn: Long?,

    @SerializedName("username")
    @Expose
    val username: String?,

    ) : Parcelable {
    val isSuccess:Boolean
        get() = tokenType != null
}