package com.samgye.orderapp.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.samgye.orderapp.activity.viewmodel.NoticeViewModel
import com.samgye.orderapp.api.ApiClient
import com.samgye.orderapp.api.request.NoticeDetailRequest
import com.samgye.orderapp.data.NoticeDetail
import com.samgye.orderapp.databinding.FragmentNoticeDetailBinding

class NoticeDetailFragment(noticeSeq: Int, viewModel: NoticeViewModel) : Fragment() {
    private val TAG = this::class.java.simpleName
    private lateinit var binding: FragmentNoticeDetailBinding
    private val noticeDetailRequest = NoticeDetailRequest(noticeSeq)
    private val noticeViewModel = viewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNoticeDetailBinding.inflate(layoutInflater, container, false)
        binding.noticeViewModel = noticeViewModel
        binding.lifecycleOwner = this

        noticeViewModel.loadNoticeDetail()

        return binding.root
    }
}