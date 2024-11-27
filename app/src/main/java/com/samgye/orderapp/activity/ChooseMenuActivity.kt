package com.samgye.orderapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.samgye.orderapp.R
import com.samgye.orderapp.activity.viewmodel.ChooseMenuViewModel
import com.samgye.orderapp.activity.viewmodel.MenuViewModel
import com.samgye.orderapp.activity.viewmodel.PopupViewModel
import com.samgye.orderapp.adapter.CategoryListAdapter
import com.samgye.orderapp.databinding.ActivityChooseMenuBinding
import com.samgye.orderapp.databinding.ActivityMenuListBinding

class ChooseMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChooseMenuBinding
    private lateinit var chooseMenuViewModel: ChooseMenuViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChooseMenuBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        chooseMenuViewModel = ViewModelProvider(this)[ChooseMenuViewModel::class.java]

        binding.chooseMenuViewModel = chooseMenuViewModel
        binding.lifecycleOwner = this

        chooseMenuViewModel.loadChooseListData(
            intent.getStringExtra("menuTitle").toString(),
            intent.getStringExtra("menuInfo").toString(),
            intent.getIntExtra("menuSeq", 0),
            intent.getIntExtra("menuSize", 0),
            intent.getStringExtra("menuImgUrl").toString(),
            intent.getIntExtra("menuPrice", 0))

        chooseMenuViewModel.menu_size.observe(this) { size ->
            var isEnable = false
            var res: Int
            isEnable = if (size > 0) {
                res = R.drawable.border_radius_state_true_12px
                true
            } else {
                res = R.drawable.border_radius_state_false_12px
                false
            }

            binding.tvChooseMenuOrder.setBackgroundResource(res)
            binding.tvChooseMenuOrder.isEnabled = isEnable
        }

        binding.tvChooseMenuOrder.setOnClickListener {
            // 장바구니로 데이터 넣기
            finish()
        }
    }
}