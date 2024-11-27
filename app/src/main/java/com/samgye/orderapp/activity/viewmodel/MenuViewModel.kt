package com.samgye.orderapp.activity.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.samgye.orderapp.api.ApiClient
import com.samgye.orderapp.data.CategoryInfo
import com.samgye.orderapp.data.ChooseListInfo
import com.samgye.orderapp.data.MenuInfo

class MenuViewModel: ViewModel() {
    private val _menu_data = MutableLiveData<List<CategoryInfo>>()

    val menu_data: LiveData<List<CategoryInfo>>
        get() = _menu_data

    private val _menu_list_title = MutableLiveData<String>()
    val menu_list_title: LiveData<String>
        get() = _menu_list_title

    private val _choose_list_data = MutableLiveData<ChooseListInfo>()
    val choose_list_data: LiveData<ChooseListInfo>
        get() = _choose_list_data

    private val _is_popular_menu = MutableLiveData<Boolean>()
    val is_popular_menu: LiveData<Boolean>
        get() = _is_popular_menu


    fun loadMenuData(title: String) {
        _menu_list_title.value = title
        ApiClient.instance.getMenuInfo() { menu, error ->
            if (error != null) {
                _menu_data.value = null
                // error
            } else {
                if (menu != null) {
                    menu
                    val categoryList = menu.map { responseCategory ->
                        CategoryInfo(
                            categorySeq = responseCategory.categorySeq,
                            categoryNm = responseCategory.categoryNm,
                            menu = responseCategory.menu?.map { responseMenu ->
                                MenuInfo(
                                    menuSeq = responseMenu.menuSeq,
                                    menuTitle = responseMenu.menuTitle,
                                    menuInfo = responseMenu.menuInfo,
                                    menuImgUrl = responseMenu.menuImgUrl,
                                    menuPrice = responseMenu.menuPrice,
                                    popularYn = responseMenu.popularYn.equals("Y")
                                )
                            }
                        )
                    }

                    val popularMenuList = menu.flatMap { responseCategory ->
                        responseCategory.menu?.filter { it.popularYn == "Y" }?.map { responseMenu ->
                            MenuInfo(
                                menuSeq = responseMenu.menuSeq,
                                menuTitle = responseMenu.menuTitle,
                                menuInfo = responseMenu.menuInfo,
                                menuImgUrl = responseMenu.menuImgUrl,
                                menuPrice = responseMenu.menuPrice,
                                popularYn = true
                            )
                        } ?: emptyList()
                    }

                    val finalCategoryList = if (popularMenuList.isNotEmpty()) {
                        listOf(
                            CategoryInfo(
                                categorySeq = 0,
                                categoryNm = "인기메뉴",
                                menu = popularMenuList
                            )
                        ) + categoryList
                    } else {
                        categoryList
                    }

                    _menu_data.value = finalCategoryList
                }
            }
        }
    }
}