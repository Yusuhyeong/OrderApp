package com.samgye.orderapp.data

data class MenuInfo (
    val menuSeq: Int? = 0,
    val menuTitle: String? = "",
    val menuInfo: String? = "",
    val menuImgUrl: String? = "",
    val menuPrice: String? = "",
    val popularYn: Boolean? = false
)
