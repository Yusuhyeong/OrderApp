package com.samgye.orderapp.data

data class UserInfo(
    val userName: String? = "",
    val snsType: String? = "",
    val point: Int? = 0,
    val loginStatus: Boolean
)
