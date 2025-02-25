package com.samgye.orderapp.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.samgye.orderapp.activity.viewmodel.OrderListViewModel
import com.samgye.orderapp.adapter.OrderDetailAdapter
import com.samgye.orderapp.databinding.FragmentOrderDetailBinding

class OrderDetailFragment(viewModel: OrderListViewModel) : Fragment() {
    private val TAG = this::class.java.simpleName
    private lateinit var binding: FragmentOrderDetailBinding
    private val orderViewModel = viewModel
    private lateinit var orderDetailAdapter: OrderDetailAdapter

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

        val menuDetailInfo = orderViewModel.order_detail_info.value
        orderDetailAdapter = OrderDetailAdapter()
        binding.rvOrderList.adapter = orderDetailAdapter
        orderDetailAdapter.submitList(menuDetailInfo?.menuList)

        var totalPrice: Int = 0
        val usePoint = menuDetailInfo?.usePoint!!
        val lastPrice: Int = totalPrice - usePoint

        Log.d(TAG, "========================================")
        Log.d(TAG, "orderType : ${menuDetailInfo?.orderType}")
        Log.d(TAG, "orderStat : ${menuDetailInfo?.orderStat}")
        Log.d(TAG, "usePoint : ${menuDetailInfo?.usePoint}")
        Log.d(TAG, "regDttm : ${menuDetailInfo?.regDttm}")
        Log.d(TAG, "----------------------------------------")

        for (i:Int in menuDetailInfo?.menuList!!.indices) {
            Log.d(TAG, "menuTitle : ${menuDetailInfo.menuList[i].menuTitle}")
            Log.d(TAG, "itemPrice : ${menuDetailInfo.menuList[i].itemPrice}")
            Log.d(TAG, "menuSize : ${menuDetailInfo.menuList[i].menuSize}")

            totalPrice += (menuDetailInfo.menuList[i].itemPrice?.toInt()
                ?: 0) * menuDetailInfo.menuList[i].menuSize!!
        }
        Log.d(TAG, "========================================")


        binding.tvTotalPrice.text = lastPrice.toString()
        binding.tvUsePoint.text = usePoint.toString()
        binding.tvMenuPrice.text = totalPrice.toString()

        return binding.root
    }
}