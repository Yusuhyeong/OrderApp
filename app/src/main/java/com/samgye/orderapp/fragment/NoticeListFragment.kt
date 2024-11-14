package com.samgye.orderapp.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.samgye.orderapp.activity.viewmodel.NoticeViewModel
import com.samgye.orderapp.adapter.NoticeListAdapter
import com.samgye.orderapp.api.ApiClient
import com.samgye.orderapp.data.NoticeItem
import com.samgye.orderapp.databinding.FragmentNoticeListBinding

class NoticeListFragment : Fragment() {
    private val TAG = this::class.java.simpleName
    private lateinit var binding: FragmentNoticeListBinding
    private lateinit var noticeListAdapter: NoticeListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNoticeListBinding.inflate(layoutInflater, container, false)
        val noticeViewModel = ViewModelProvider(this)[NoticeViewModel::class.java]
        binding.noticeViewModel = noticeViewModel
        binding.lifecycleOwner = this

        noticeListAdapter = NoticeListAdapter(noticeViewModel)
        binding.rvNoticeList.adapter = noticeListAdapter

        ApiClient.instance.getAllNotice { notice, error ->
            if (error != null) {
                Log.d(TAG, "ERROR")
            } else {
                val noticeItems: List<NoticeItem> = notice?.data?.map { noticeInfo ->
                    NoticeItem(
                        noticeSeq = noticeInfo.noticeSeq,
                        noticeTitle = noticeInfo.noticeTitle,
                        regDttm = noticeInfo.regDttm,
                        regrNm = noticeInfo.regrNm
                    )
                } ?: emptyList()

                noticeListAdapter.submitList(noticeItems)
            }

        }

        return binding.root
    }
}