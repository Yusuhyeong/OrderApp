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
//    private val fragmentTransaction = supportFragmentManager.beginTransaction()
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
//                if (::noticeListFragment.isInitialized) {
                    val fragmentTransaction = supportFragmentManager.beginTransaction()
                    noticeDetailFragment = NoticeDetailFragment(seq, viewModel)
                    fragmentTransaction.add(R.id.fl_notice, noticeDetailFragment, "NoticeDetailFragment")
                    fragmentTransaction.show(noticeListFragment).hide(noticeListFragment).commit()
                    viewModel.setIsGoHome(false)
//                }
            }
        }

        viewModel.is_back_click.observe(this) { back ->
            if (back) {
                finish()
            } else {
                if (::noticeDetailFragment.isInitialized && ::noticeListFragment.isInitialized) {
                    val fragmentTransaction = supportFragmentManager.beginTransaction()
                    fragmentTransaction.show(noticeListFragment).remove(noticeDetailFragment).commit()
                    viewModel.setIsGoHome(true)
                }
            }
        }
    }

    private fun setFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        if (isFromCategory == false) {
            val noticeSeq = intent.getIntExtra("noticeSeq", 1)
            noticeDetailFragment = NoticeDetailFragment(noticeSeq, viewModel)
            fragmentTransaction.add(R.id.fl_notice, noticeDetailFragment, "NoticeDetailFragment")
            fragmentTransaction.show(noticeDetailFragment)
        } else {
            noticeListFragment = NoticeListFragment(viewModel)
            fragmentTransaction.add(R.id.fl_notice, noticeListFragment, "NoticeListFragment")
            fragmentTransaction.show(noticeListFragment)
        }
        fragmentTransaction.commit()
    }

    override fun onBackPressed() {
        // 원하는 동작을 여기에 작성
        viewModel.backClick()
    }
}