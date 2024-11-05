package com.samgye.orderapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import androidx.lifecycle.ViewModelProvider
import com.samgye.orderapp.R
import com.samgye.orderapp.activity.viewmodel.HomeViewModel
import com.samgye.orderapp.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private var isMenuVisible = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        binding.homeViewModel = viewModel
        binding.lifecycleOwner = this

//        viewModel.isMenuVisible.observe(this) { isVisible ->
//            if (isVisible) {
//                binding.clMenu.startAnimation(AnimationUtils.loadAnimation(this, R.anim.menu_slide_in))
//                binding.clMenu.visibility = View.VISIBLE
//            } else {
//                binding.clMenu.startAnimation(AnimationUtils.loadAnimation(this, R.anim.menu_slide_out))
//                binding.clMenu.visibility = View.GONE
//            }
//        }

        viewModel.selected_id.observe(this) { id ->
            Log.d("HomeActivity", "onClick : ${id.toString()}")
            when(id) {
                R.id.cl_dummy_menu.toString() -> {
                    if (isMenuVisible) {
                        binding.clMenu.startAnimation(AnimationUtils.loadAnimation(this, R.anim.menu_slide_out))
                        binding.clMenu.visibility = View.GONE
                        binding.clDummyMenu.visibility = View.GONE
                        isMenuVisible = false
                    }
                }

                R.id.iv_menu.toString() -> {
                    if (!isMenuVisible) {
                        binding.clMenu.startAnimation(AnimationUtils.loadAnimation(this, R.anim.menu_slide_in))
                        binding.clMenu.visibility = View.VISIBLE
                        binding.clDummyMenu.visibility = View.VISIBLE
                        isMenuVisible = true
                    }
                }
            }

        }
    }
}