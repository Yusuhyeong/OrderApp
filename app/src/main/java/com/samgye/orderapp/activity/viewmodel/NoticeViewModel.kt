package com.samgye.orderapp.activity.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.samgye.orderapp.api.ApiClient
import com.samgye.orderapp.api.request.NoticeDetailRequest
import com.samgye.orderapp.data.NoticeDetail
import com.samgye.orderapp.data.NoticeInfo
import com.samgye.orderapp.data.NoticeItem
import com.samgye.orderapp.utils.SystemUtil

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
    private val _notice_list = MutableLiveData<List<NoticeItem>>()
    val notice_list: LiveData<List<NoticeItem>>
        get() = _notice_list

    private val _notice_detail = MutableLiveData<NoticeDetail>()
    val notice_detail: LiveData<NoticeDetail>
        get() = _notice_detail

    fun setNoticeDetailData(noticeDetail: NoticeDetail) {
        _noticeData.value = noticeDetail
        _is_notice_img.value = noticeDetail.noticeImg.isNullOrEmpty()
    }

    fun noticeClick(seq: Int) {
        _select_seq.value = seq
    }

    fun loadNoticeList() {
        ApiClient.instance.getAllNotice { notice, error ->
            if (error != null) {
//                Log.d(TAG, "ERROR")
            } else {
                val noticeItems: List<NoticeItem> = notice?.data?.map { noticeInfo ->
                    NoticeItem(
                        noticeSeq = noticeInfo.noticeSeq,
                        noticeTitle = noticeInfo.noticeTitle,
                        regDttm = SystemUtil.formatToDateOnly(noticeInfo.regDttm),
                        regrNm = noticeInfo.regrNm
                    )
                } ?: emptyList()

                _notice_list.value = noticeItems
            }

        }
    }

    fun loadNoticeDetail() {
        _is_detail_loading.value = true
        val noticeSeq = NoticeDetailRequest(select_seq.value)
        ApiClient.instance.getDetailNotice(noticeSeq) { notice, error ->
            if (error != null) {
//                Log.e(TAG, "getDetailNotice error")
            } else {
                if (notice != null) {
                    val noticeData = NoticeDetail(notice.data?.noticeSeq, notice.data?.noticeTitle, notice.data?.noticeCont, notice.data?.regDttm, notice.data?.regrNm, notice.data?.noticeImg)
                    _notice_detail.value = noticeData
                }

                _is_detail_loading.value = false
            }
        }
    }
}