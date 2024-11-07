package com.samgye.orderapp.utils

object Constants {

    const val IS_DEBUG = true
    const val BASIC = "Basic"
    const val BEARER = "Bearer"
    const val AUTHORIZATION = "Authorization"

    const val BASE_URL = "http://10.112.59.189:8081/"
//    const val BASE_URL = "http://10.112.60.173:8081/"

    const val LOGIN_PATH = "api/auth/login"
    const val REFRESH_PATH = "api/auth/refresh"
    const val USER_INFO_PATH = "api/user/me"
    const val USERNAME_UPDATE = "api/userInfo/updateUserName"

    const val SEARCH_PATH = "/search"

    // LOGIN CODE
    const val KAKAO_LOGIN_ERROR = "KAKAO_LOGIN_ERROR"
    const val UNREGISTERED_USER = "UNREGISTERED_USER"
}