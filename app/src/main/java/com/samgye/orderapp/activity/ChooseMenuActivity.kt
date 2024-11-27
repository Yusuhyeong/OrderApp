package com.samgye.orderapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.samgye.orderapp.R
import com.samgye.orderapp.Samgye
import com.samgye.orderapp.activity.viewmodel.ChooseMenuViewModel
import com.samgye.orderapp.activity.viewmodel.MenuViewModel
import com.samgye.orderapp.activity.viewmodel.PopupViewModel
import com.samgye.orderapp.adapter.CategoryListAdapter
import com.samgye.orderapp.data.CartMenuInfo
import com.samgye.orderapp.databinding.ActivityChooseMenuBinding
import com.samgye.orderapp.databinding.ActivityMenuListBinding
import com.samgye.orderapp.utils.PersistentKVStore
import com.samgye.orderapp.utils.SharedPrefsWrapper

class ChooseMenuActivity : AppCompatActivity() {
    private val TAG = this::class.java.simpleName
    private lateinit var binding: ActivityChooseMenuBinding
    private lateinit var chooseMenuViewModel: ChooseMenuViewModel
    private val appCache: PersistentKVStore = SharedPrefsWrapper(Samgye.mSharedPreferences)
    private val menuKey = "menuList"

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

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.tvChooseMenuOrder.setOnClickListener {
            val menuSeq = chooseMenuViewModel.menu_seq.value ?: 0
            val menuSize = chooseMenuViewModel.menu_size.value ?: 0

            val cartMenuInfo = CartMenuInfo(
                menuSeq = menuSeq,
                menuSize = menuSize
            )

            val gson = Gson()

            val beforeMenu = appCache.getString(menuKey, null)
            val afterMenu: MutableList<CartMenuInfo> = if (beforeMenu != null) {
                gson.fromJson(beforeMenu, object : TypeToken<MutableList<CartMenuInfo>>() {}.type)
            } else {
                mutableListOf()
            }

            afterMenu.addAll(listOf(cartMenuInfo))

            val reCartMenuInfo = gson.toJson(afterMenu)
            Log.d(TAG, "reCartMenuInfo : $reCartMenuInfo")
            appCache.putString(menuKey, reCartMenuInfo)
            appCache.commit()

            finish()
        }
    }
}