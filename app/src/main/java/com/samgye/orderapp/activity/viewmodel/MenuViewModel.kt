package com.samgye.orderapp.activity.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.samgye.orderapp.api.ApiClient
import com.samgye.orderapp.data.CategoryInfo
import com.samgye.orderapp.data.MenuInfo

class MenuViewModel: ViewModel() {
    private val _menu_data = MutableLiveData<List<CategoryInfo>>()

    // 외부에서 관찰할 수 있는 LiveData
    val menu_data: LiveData<List<CategoryInfo>>
        get() = _menu_data

    fun loadMenuData() {
        ApiClient.instance.getMenuInfo() { menu, error ->
            if (error != null) {
                // error
            } else {
                if (menu != null) {
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
                                    popularYn = responseMenu.popularYn
                                )
                            }
                        )
                    }

                    _menu_data.postValue(categoryList)
                }
            }
        }
    }
}