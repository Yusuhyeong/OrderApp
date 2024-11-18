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

    private val _select_seq = MutableLiveData<Int>()
    val select_seq: LiveData<Int>
        get() = _select_seq

    private val _is_detail_loading = MutableLiveData<Boolean>()
    val is_detail_loading: LiveData<Boolean>
        get() = _is_detail_loading

    fun setNoticeDetailData(noticeDetail: NoticeDetail) {
        _noticeData.value = noticeDetail
        _is_notice_img.value = noticeDetail.noticeImg.isNullOrEmpty()
    }

    fun noticeClick(seq: Int) {
        _select_seq.value = seq
    }

    fun setDetailLoading(loading: Boolean) {
        _is_detail_loading.value = loading
    }
}