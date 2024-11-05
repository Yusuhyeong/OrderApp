package com.samgye.orderapp.api.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

@Parcelize
data class TokenResponse(
    @Expose
    val tokenType: String,
    @Expose
    val accessToken: String,
    @Expose
    val refreshToken: String,
    @Expose
    val expiresIn: Long,
    @Expose
    val username: String?,

    ) : Parcelable