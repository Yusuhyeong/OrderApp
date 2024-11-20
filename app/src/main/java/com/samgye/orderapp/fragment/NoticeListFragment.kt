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
import com.samgye.orderapp.utils.SystemUtil

class NoticeListFragment(viewModel: NoticeViewModel) : Fragment() {
    private val TAG = this::class.java.simpleName
    private lateinit var binding: FragmentNoticeListBinding
    private lateinit var noticeListAdapter: NoticeListAdapter
    private val noticeViewModel = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNoticeListBinding.inflate(layoutInflater, container, false)
//        val noticeViewModel = ViewModelProvider(this)[NoticeViewModel::class.java]
        binding.noticeViewModel = noticeViewModel
        binding.lifecycleOwner = this

        noticeListAdapter = NoticeListAdapter(noticeViewModel)
        binding.rvNoticeList.adapter = noticeListAdapter

        noticeViewModel.loadNoticeList()

        noticeViewModel.notice_list.observe(requireActivity()) { list ->
            noticeListAdapter.submitList(list)
        }

        return binding.root
    }
}