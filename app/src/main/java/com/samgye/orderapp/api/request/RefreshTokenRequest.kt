package com.samgye.orderapp.api.request

import com.google.gson.annotations.Expose

data class RefreshTokenRequest(@Expose val refreshToken: String)