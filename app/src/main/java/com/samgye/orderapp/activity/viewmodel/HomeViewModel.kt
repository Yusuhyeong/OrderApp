package com.samgye.orderapp.activity.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.samgye.orderapp.api.ApiClient
import com.samgye.orderapp.data.NoticeInfo

class HomeViewModel : ViewModel() {
    val _selected_id = SingleLiveEvent<String>()
    private val _is_menu_visible = MutableLiveData<Boolean>(false)
    val is_menu_visible: LiveData<Boolean>
        get() = _is_menu_visible
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

    fun clickMenu(view: View) {
        _selected_id.value = view.id.toString()
    }

    fun setMenuVisible(status: Boolean) {
        _is_menu_visible.value = status
    }

    fun setNoticeInfo(noticeInfo: NoticeInfo) {
        _noticeData.value = noticeInfo
    }
}