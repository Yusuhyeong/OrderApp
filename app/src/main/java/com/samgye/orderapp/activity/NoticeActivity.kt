package com.samgye.orderapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.samgye.orderapp.R
import com.samgye.orderapp.activity.viewmodel.NoticeViewModel
import com.samgye.orderapp.databinding.ActivityNoticeBinding
import com.samgye.orderapp.fragment.NoticeDetailFragment
import com.samgye.orderapp.fragment.NoticeListFragment

class NoticeActivity : AppCompatActivity() {
    private val TAG = this::class.java.simpleName
    private lateinit var binding: ActivityNoticeBinding
    private var isFromCategory: Boolean? = null
    private lateinit var noticeListFragment: NoticeListFragment
    private lateinit var noticeDetailFragment: NoticeDetailFragment
    private lateinit var viewModel: NoticeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice)

        binding = ActivityNoticeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        viewModel = ViewModelProvider(this)[NoticeViewModel::class.java]
        binding.noticeViewModel = viewModel
        binding.lifecycleOwner = this

        isFromCategory = intent.getBooleanExtra("isFromCategory", false)

        setFragment()

        viewModel.select_seq.observe(this) { seq ->
            Log.d(TAG, "select_seq observe")
            if (seq != null) {
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                noticeDetailFragment = NoticeDetailFragment(seq, viewModel)
                fragmentTransaction.add(R.id.fl_notice, noticeDetailFragment, "NoticeDetailFragment").addToBackStack(null).commit()
            }
            Log.d(TAG, "backStackCount : ${supportFragmentManager.backStackEntryCount}")
        }

        binding.ivNoticeBack.setOnClickListener {
            Log.d(TAG, "backStackCount : ${supportFragmentManager.backStackEntryCount}")
            Log.d(TAG, "is_back_click observe")
            if (supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.popBackStack()
//                    viewModel.setIsBackClick()
            } else {
                super.onBackPressed()
            }
        }
    }

    private fun setFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val seq = intent.getIntExtra("noticeSeq", -1)
        if (seq == -1) {
            // noticeList
            noticeListFragment = NoticeListFragment(viewModel)
            fragmentTransaction.add(R.id.fl_notice, noticeListFragment, "NoticeListFragment").commit()
        } else {
            // noticeDetail
            noticeDetailFragment = NoticeDetailFragment(seq, viewModel)
            fragmentTransaction.add(R.id.fl_notice, noticeDetailFragment, "NoticeDetailFragment").commit()
        }

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