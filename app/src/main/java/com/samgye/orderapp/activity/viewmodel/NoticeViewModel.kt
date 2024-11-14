package com.samgye.orderapp.activity.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.samgye.orderapp.data.NoticeDetail
import com.samgye.orderapp.data.NoticeInfo

class NoticeViewModel : ViewModel() {
    private val _noticeData = MutableLiveData<NoticeDetail>()
    val noticeData: LiveData<NoticeDetail>
        get() = _noticeData

    private val _is_notice_img = MutableLiveData<Boolean>()
    val is_notice_img: LiveData<Boolean>
        get() = _is_notice_img

    private val _is_go_home = MutableLiveData<Boolean>(true)
    val is_go_home: LiveData<Boolean>
        get() = _is_go_home

    fun setNoticeDetailData(noticeDetail: NoticeDetail) {
        _noticeData.value = noticeDetail
        _is_notice_img.value = noticeDetail.noticeImg.isNullOrEmpty()

        Log.d("TESTLOG", _is_notice_img.value.toString())
    }

    fun setNoticeInfoData(noticeInfo: NoticeInfo) {

    }
}