package com.samgye.orderapp.data

data class CategoryInfo (
    val categorySeq: Int? = 0,
    val categoryNm: String? = "",
    val menu: List<MenuInfo>?
)
