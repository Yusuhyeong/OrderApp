package com.samgye.orderapp.activity.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.samgye.orderapp.api.ApiClient
import com.samgye.orderapp.data.EventInfo
import com.samgye.orderapp.data.NoticeInfo

class HomeViewModel : ViewModel() {
    val selected_id = SingleLiveEvent<String>()
    private val _is_menu_visible = MutableLiveData<Boolean>(false)
    val is_menu_visible: LiveData<Boolean>
        get() = _is_menu_visible
    private val _noticeData = MutableLiveData<NoticeInfo>()
    val noticeData: LiveData<NoticeInfo>
        get() = _noticeData

    private val _event_list = MutableLiveData<List<EventInfo>>()
    val event_list: LiveData<List<EventInfo>>
        get() = _event_list

    private val _click_event_info = MutableLiveData<EventInfo>()
    val click_event_info: LiveData<EventInfo>
        get() = _click_event_info

    private val _current_event_page = MutableLiveData<Int>()
    val current_event_page: LiveData<Int>
        get() = _current_event_page

    private val _total_event_page = MutableLiveData<Int>()
    val total_event_page: LiveData<Int>
        get() = _total_event_page

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
        selected_id.value = view.id.toString()
    }

    fun setMenuVisible(status: Boolean) {
        _is_menu_visible.value = status
    }

    fun setNoticeInfo(noticeInfo: NoticeInfo) {
        _noticeData.value = noticeInfo
    }

    fun loadEventInfo() {
        // Api 호출을 통한 데이터로 수정 필요
        val baseEventList = listOf(
            EventInfo(1, "event1", "event1 입니다", "2024-12-05", "관리자", "https://postfiles.pstatic.net/MjAyMjAxMDdfNDcg/MDAxNjQxNDgzMDE3OTMz.i2FVz1H6MiemDnlFLiy6y4e0CloOintzDPZDRXeMXGMg.uhLCrnhOs7foDjg0HvfrgdS94zGWqN3kaHrHJLBBngsg.JPEG.kjy6588/20220106%EF%BC%BF140908.jpg?type=w773"),
            EventInfo(2, "event2", "event2 입니다", "2024-12-05", "관리자", "https://postfiles.pstatic.net/MjAyMjAxMDdfMjAx/MDAxNjQxNDgzMDI4MDU1.XOan35fQ-hDv2ro_2a0S2_nFLezeSueQvwKLKJeSCEQg._-UoLaJcsAfksFvDzaX0gOuGvY9niDm3ZXwCQDKGHMcg.JPEG.kjy6588/20220106%EF%BC%BF151939.jpg?type=w773"),
            EventInfo(3, "event3", "event3 입니다", "2024-12-05", "관리자", "https://postfiles.pstatic.net/MjAyMjAxMDdfMTY0/MDAxNjQxNDgzMDI0OTU3.dB-Jkv4ZdyhKrcHI1MYcJ3H6z-tiw6ZPofilj6nsBegg.vKh67YUpvovM6TO0MfUHNI9BEMadUvtAhWY-BQdgdCUg.JPEG.kjy6588/20220106%EF%BC%BF141054.jpg?type=w773")
        )

        val eventList: MutableList<EventInfo> = mutableListOf()
        for (i: Int in 0..<50) {
            for (element in baseEventList) {
                eventList.add(element)
            }
        }

        _event_list.value = baseEventList
    }

    fun clickEvent(eventInfo: EventInfo) {
        _click_event_info.value = eventInfo
    }

    fun setCurrentPage(currentPage: Int, eventListSize: Int) {
        if (currentPage % eventListSize == 0) {
            _current_event_page.value = eventListSize
        } else {
            _current_event_page.value = currentPage % eventListSize
        }
    }

    fun setTotalPage(totalPage: Int) {
        _total_event_page.value = totalPage
    }
}