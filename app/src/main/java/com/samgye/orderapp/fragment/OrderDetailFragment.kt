package com.samgye.orderapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.samgye.orderapp.activity.viewmodel.OrderListViewModel
import com.samgye.orderapp.adapter.NoticeListAdapter
import com.samgye.orderapp.databinding.FragmentOrderDetailBinding
import com.samgye.orderapp.databinding.FragmentOrderListBinding

class OrderDetailFragment(viewModel: OrderListViewModel) : Fragment() {
    private val TAG = this::class.java.simpleName
    private lateinit var binding: FragmentOrderDetailBinding
    private lateinit var orderListAdapter: NoticeListAdapter
    private val orderViewModel = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderDetailBinding.inflate(layoutInflater, container, false)
        binding.orderListViewModel = orderViewModel
        binding.lifecycleOwner = this

//        noticeListAdapter = NoticeListAdapter(orderViewModel)
//        binding.rvNoticeList.adapter = orderListAdapter

        return binding.root
    }
}