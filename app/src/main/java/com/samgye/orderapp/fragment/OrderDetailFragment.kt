package com.samgye.orderapp.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.samgye.orderapp.activity.viewmodel.OrderListViewModel
import com.samgye.orderapp.adapter.NoticeListAdapter
import com.samgye.orderapp.data.OrderDetailInfo
import com.samgye.orderapp.databinding.FragmentOrderDetailBinding
import com.samgye.orderapp.databinding.FragmentOrderListBinding

class OrderDetailFragment(viewModel: OrderListViewModel) : Fragment() {
    private val TAG = this::class.java.simpleName
    private lateinit var binding: FragmentOrderDetailBinding
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

//        val orderType: String?,
//        val orderStat: String?,
//        val usePoint: Int?,
//        val regDttm: String?,
//        val menuList: List<OrderDetailInfo>?
        val testData = orderViewModel.order_detail_info.value
        Log.d(TAG, "========================================")
        Log.d(TAG, "orderType : ${testData?.orderType}")
        Log.d(TAG, "orderStat : ${testData?.orderStat}")
        Log.d(TAG, "usePoint : ${testData?.usePoint}")
        Log.d(TAG, "regDttm : ${testData?.regDttm}")
//        val menuTitle: String?,
//        val itemPrice: String?,
//        val menuSize: Int?
        Log.d(TAG, "----------------------------------------")
        for (i:Int in testData?.menuList!!.indices) {
            Log.d(TAG, "menuTitle : ${testData.menuList[i].menuTitle}")
            Log.d(TAG, "itemPrice : ${testData.menuList[i].itemPrice}")
            Log.d(TAG, "menuSize : ${testData.menuList[i].menuSize}")
        }
        Log.d(TAG, "========================================")


//        noticeListAdapter = NoticeListAdapter(orderViewModel)
//        binding.rvNoticeList.adapter = orderListAdapter

        return binding.root
    }
}