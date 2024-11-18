package com.samgye.orderapp.activity.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
    private val _user_data = MutableLiveData<MyData>()
    val user_data: LiveData<MyData>
        get() = _user_data
    private val _noticeData = MutableLiveData<NoticeInfo>()
    val noticeData: LiveData<NoticeInfo>
        get() = _noticeData

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

    fun setMenuVisible(status: Boolean) {
        _is_menu_visible.value = status
    }

    fun getClickId(view: View) {
        _selected_id.value = view.id.toString()
    }

    fun setLoginStatus(isLogin: Boolean) {
        _is_login_status.value = isLogin
    }

    fun setUserData(data: MyData) {
        _user_data.value = data
    }

    fun setNoticeInfo(noticeInfo: NoticeInfo) {
        _noticeData.value = noticeInfo
    }

    fun clearHome() {
        _user_data.value = null
        _is_login_status.value = false
        _is_menu_visible.value = false
    }
}