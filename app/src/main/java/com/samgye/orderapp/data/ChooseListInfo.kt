package com.samgye.orderapp.data

data class ChooseListInfo(
    val categorySeq: Int? = 0,
    val categoryNm: String? = "",
    val menuSeq: Int? = 0,
    val menuTitle: String? = "",
    val menuInfo: String? = "",
    val menuImgUrl: String? = "",
    val menuPrice: String? = "",
    val popularYn: String? = ""
)
