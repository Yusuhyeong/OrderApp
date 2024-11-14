package com.samgye.orderapp.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.samgye.orderapp.activity.viewmodel.NoticeViewModel
import com.samgye.orderapp.api.ApiClient
import com.samgye.orderapp.api.request.NoticeDetailRequest
import com.samgye.orderapp.data.NoticeDetail
import com.samgye.orderapp.databinding.FragmentNoticeDetailBinding

class NoticeDetailFragment(noticeSeq: Int) : Fragment() {
    private val TAG = this::class.java.simpleName
    private lateinit var binding: FragmentNoticeDetailBinding
    private val noticeDetailRequest = NoticeDetailRequest(noticeSeq)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNoticeDetailBinding.inflate(layoutInflater, container, false)
        val noticeViewModel = ViewModelProvider(this)[NoticeViewModel::class.java]
        binding.noticeViewModel = noticeViewModel
        binding.lifecycleOwner = this

        ApiClient.instance.getDetailNotice(noticeDetailRequest) { notice, error ->
            if (error != null) {
                Log.e(TAG, "getDetailNotice error")
            } else {
                if (notice != null) {
                    val noticeData = NoticeDetail(notice.data?.noticeSeq, notice.data?.noticeTitle, notice.data?.noticeCont, notice.data?.regDttm, notice.data?.regrNm, notice.data?.noticeImg)
                    noticeViewModel.setNoticeDetailData(noticeData)
                    Log.d(TAG, "SUCCESS")
                }
            }
        }

        return binding.root
    }
}