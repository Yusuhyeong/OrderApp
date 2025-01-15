package com.samgye.orderapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.samgye.orderapp.R
import com.samgye.orderapp.activity.viewmodel.NoticeViewModel
import com.samgye.orderapp.activity.viewmodel.OrderListViewModel
import com.samgye.orderapp.api.ApiClient
import com.samgye.orderapp.databinding.ActivityNoticeBinding
import com.samgye.orderapp.databinding.ActivityOrderListBinding
import com.samgye.orderapp.fragment.NoticeDetailFragment
import com.samgye.orderapp.fragment.NoticeListFragment
import com.samgye.orderapp.fragment.OrderDetailFragment
import com.samgye.orderapp.fragment.OrderListFragment

class OrderListActivity : AppCompatActivity() {
    private val TAG = this::class.java.simpleName
    private lateinit var binding: ActivityOrderListBinding
    private lateinit var viewModel: OrderListViewModel
    private lateinit var orderListFragment: OrderListFragment
    private lateinit var orderDetailFragment: OrderDetailFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOrderListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProvider(this)[OrderListViewModel::class.java]
        binding.orderListViewModel = viewModel
        binding.lifecycleOwner = this

        setFragment()

        viewModel.loadOrderList()
    }

    private fun setFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        orderListFragment = OrderListFragment(viewModel)
        fragmentTransaction.add(R.id.fl_order_list, orderListFragment, "OrderListFragment").commit()
        // order click 이벤트 발생시 detail로, 아니라면 list로
        // 스택 관리는 해야함

//        val seq = intent.getIntExtra("fromOrder", -1)
//        if (seq == -1) {
//            // noticeList
//            orderListFragment = OrderListFragment(viewModel)
//            fragmentTransaction.add(R.id.fl_notice, orderListFragment, "OrderListFragment").commit()
//        } else {
//            // noticeDetail
//            viewModel.noticeClick(seq)
//            noticeDetailFragment = NoticeDetailFragment(seq, viewModel)
//            fragmentTransaction.add(R.id.fl_notice, noticeDetailFragment, "NoticeDetailFragment").commit()
//        }

        Log.d(TAG, "backStackCount : ${supportFragmentManager.backStackEntryCount}")
    }
}