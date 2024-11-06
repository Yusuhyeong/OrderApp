package com.samgye.orderapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import androidx.lifecycle.ViewModelProvider
import com.samgye.orderapp.R
import com.samgye.orderapp.activity.viewmodel.HomeViewModel
import com.samgye.orderapp.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        binding.homeViewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.is_menu_visible.observe(this) { visible ->
            Log.d("HomeActivity", "menu status : $visible")

            if (visible) {
                binding.clMenu.startAnimation(AnimationUtils.loadAnimation(this, R.anim.menu_slide_in))
            } else {
                binding.clMenu.startAnimation(AnimationUtils.loadAnimation(this, R.anim.menu_slide_out))
            }
        }

        viewModel.selected_id.observe(this) { id ->
            Log.d("HomeActivity", "onClick : ${id.toString()}")
            when(id) {
                // id별 행동 추가
            }
        }
    }
}