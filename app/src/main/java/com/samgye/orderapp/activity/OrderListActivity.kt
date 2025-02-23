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

        viewModel.order_detail_info.observe(this) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            orderDetailFragment = OrderDetailFragment(viewModel)
            fragmentTransaction.add(R.id.fl_notice, orderDetailFragment, "OrderDetailFragment").addToBackStack(null).commit()

            Log.d(TAG, "backStackCount : ${supportFragmentManager.backStackEntryCount}")
        }

        binding.ivCartBack.setOnClickListener {
            if (supportFragmentManager.backStackEntryCount > 0) {
                Log.d(TAG, "backStackCount : ${supportFragmentManager.backStackEntryCount}")
                supportFragmentManager.popBackStack()
                Log.d(TAG, "backStackCount : ${supportFragmentManager.backStackEntryCount}")
            } else {
                super.onBackPressed()
            }
        }
    }

    private fun setFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        orderListFragment = OrderListFragment(viewModel)
        fragmentTransaction.add(R.id.fl_order_list, orderListFragment, "OrderListFragment").commit()

        Log.d(TAG, "backStackCount : ${supportFragmentManager.backStackEntryCount}")
    }

    override fun onBackPressed() {
        Log.d(TAG, "backStackCount : ${supportFragmentManager.backStackEntryCount}")
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}