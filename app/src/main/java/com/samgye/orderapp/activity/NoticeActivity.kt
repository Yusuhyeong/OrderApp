package com.samgye.orderapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.samgye.orderapp.R
import com.samgye.orderapp.activity.viewmodel.NoticeViewModel
import com.samgye.orderapp.data.NoticeDetail
import com.samgye.orderapp.databinding.ActivityNoticeBinding
import com.samgye.orderapp.fragment.NoticeDetailFragment
import com.samgye.orderapp.fragment.NoticeListFragment

class NoticeActivity : AppCompatActivity() {
    private val TAG = this::class.java.simpleName
    private lateinit var binding: ActivityNoticeBinding

    private var isFromCategory: Boolean? = null
    private lateinit var noticeListFragment: NoticeListFragment
    private lateinit var noticeDetailFragment: NoticeDetailFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice)

        binding = ActivityNoticeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val viewModel = ViewModelProvider(this)[NoticeViewModel::class.java]
        binding.noticeViewModel = viewModel
        binding.lifecycleOwner = this

        isFromCategory = intent.getBooleanExtra("isFromCategory", false)

        setFragment()
    }

    fun setFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        if (isFromCategory == false) {
            val noticeSeq = intent.getIntExtra("noticeSeq", 1)
            noticeDetailFragment = NoticeDetailFragment(noticeSeq)
            fragmentTransaction.replace(R.id.fl_notice, noticeDetailFragment)
            fragmentTransaction.commit()
        }
    }
}