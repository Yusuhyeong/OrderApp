package com.samgye.orderapp.activity.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.samgye.orderapp.R
import com.samgye.orderapp.data.MyData
import com.samgye.orderapp.api.ApiClient
import com.samgye.orderapp.data.NoticeInfo

class HomeViewModel : ViewModel() {
    private val _selected_id = MutableLiveData<String>()
    val selected_id: LiveData<String>
        get() = _selected_id
    private val _is_menu_visible = MutableLiveData<Boolean>(false)
    val is_menu_visible: LiveData<Boolean>
        get() = _is_menu_visible
    private val _is_login_status = MutableLiveData<Boolean>(ApiClient.instance.hasToken())
    val is_login_status: LiveData<Boolean>
        get() = _is_login_status
    private val _userData = MutableLiveData<MyData>()
    val userData: LiveData<MyData>
        get() = _userData
    private val _noticeData = MutableLiveData<NoticeInfo>()
    val noticeData: LiveData<NoticeInfo>
        get() = _noticeData
    private val _is_category_notice_click = MutableLiveData<Boolean>()
    val is_category_notice_click: LiveData<Boolean>
        get() = _is_category_notice_click

    init {
        ApiClient.instance.getLatestNotice() { notice, error ->
            if (error != null) {
                Log.e("HomeViewModel", "getLatestNotice error")
            } else {
                if (notice != null) {
                    val noticeInfo = NoticeInfo(notice.data?.noticeSeq, notice.data?.noticeTitle)
                    setNoticeInfo(noticeInfo)
                }
            }
        }
    }

    fun noticeClick(view: View) {
        when(view.id) {
            R.id.cl_notice_in_menu -> {
                _is_category_notice_click.value = true
            }
            R.id.cl_notice -> {
                _is_category_notice_click.value = false
            }
        }
    }

    fun menuClick(view: View) {
        _selected_id.value = view.id.toString()
        _is_menu_visible.value = _is_menu_visible.value?.not() ?: false
    }

    fun menuCategoryClick(view: View) {
        _selected_id.value = view.id.toString()
    }

    fun setLoginStatus(isLogin: Boolean) {
        _is_login_status.value = isLogin
    }

    fun setUserData(data: MyData) {
        _userData.value = data
    }

    fun setNoticeInfo(noticeInfo: NoticeInfo) {
        _noticeData.value = noticeInfo
    }

    fun clearHome() {
        _userData.value = null
        _is_login_status.value = false
        _is_menu_visible.value = false
    }
}