package com.samgye.orderapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.samgye.orderapp.activity.viewmodel.OrderListViewModel
import com.samgye.orderapp.adapter.NoticeListAdapter
import com.samgye.orderapp.adapter.OrderListAdapter
import com.samgye.orderapp.databinding.FragmentOrderListBinding

class OrderListFragment(viewModel: OrderListViewModel) : Fragment() {
    private val TAG = this::class.java.simpleName
    private lateinit var binding: FragmentOrderListBinding
    private lateinit var orderListAdapter: OrderListAdapter
    private val orderViewModel = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderListBinding.inflate(layoutInflater, container, false)
        binding.orderListViewModel = orderViewModel
        binding.lifecycleOwner = this

        orderListAdapter = OrderListAdapter(orderViewModel)
        binding.rvOrderList.adapter = orderListAdapter

        orderViewModel.order_list_data.observe(requireActivity()) { orderList ->
            orderListAdapter.submitList(orderList)
        }

        return binding.root
    }
}