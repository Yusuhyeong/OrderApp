package com.samgye.orderapp.utils

object Constants {

    const val IS_DEBUG = true
    const val BASIC = "Basic"
    const val BEARER = "Bearer"
    const val AUTHORIZATION = "Authorization"

    const val BASE_URL = "http://10.112.59.189:8081/" // ㄷㅅ
//    const val BASE_URL = "http://10.112.60.173:8081/" // ㅅㅎ

    const val LOGIN_PATH = "api/auth/login"
    const val REFRESH_PATH = "api/auth/refresh"
    const val USER_INFO_PATH = "api/user/me"
    const val USERNAME_UPDATE_PATH = "api/userInfo/updateUserName"
//    const val USER_POINT_INFO_PATH = "api/userPoint/getUserPoint"
    const val USER_POINT_INFO_PATH = "api/userDetail/getUserDetail"
    const val LATEST_NOTICE_PATH = "api/notice/getLatestNoticeInfo"
    const val All_NOTICE_PATH = "api/notice/getNoticeInfo"
    const val NOTICE_DETAIL_PATH = "api/notice/getNoticeDetail"
    const val MENU_INFO_PATH = "/api/menuInfo/getMenuList"
    const val MENU_ORDER_PATH = "/api/order/orderMenu"
}