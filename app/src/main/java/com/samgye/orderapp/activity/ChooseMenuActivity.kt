package com.samgye.orderapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.samgye.orderapp.R
import com.samgye.orderapp.Samgye
import com.samgye.orderapp.activity.viewmodel.ChooseMenuViewModel
import com.samgye.orderapp.data.CartMenuInfo
import com.samgye.orderapp.databinding.ActivityChooseMenuBinding
import com.samgye.orderapp.utils.Constants
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

            if (chooseMenuViewModel.menu_img_url.value != null) {
                val imgUrl = Constants.IMG_BASE_URL + chooseMenuViewModel.menu_img_url.value
                Glide.with(binding.ivChooseMenuImg.context)
                    .load(imgUrl)
                    .into(binding.ivChooseMenuImg)
            }
        }

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.tvChooseMenuOrder.setOnClickListener {
            val menuSeq = chooseMenuViewModel.menu_seq.value
            val menuSize = chooseMenuViewModel.menu_size.value
            val menuTitle = chooseMenuViewModel.menu_title.value
            val menuImgUrl = chooseMenuViewModel.menu_img_url.value
            val menuPrice = chooseMenuViewModel.menu_price.value
            var isChange = false

            var cartMenuInfo: CartMenuInfo

            val gson = Gson()
            val beforeMenu = appCache.getString(menuKey, null)
            val afterMenu: MutableList<CartMenuInfo>

            if (beforeMenu != null) {
                val beforeCartList: MutableList<CartMenuInfo> = gson.fromJson(beforeMenu, object : TypeToken<List<CartMenuInfo>>() {}.type)

                for(i: Int in beforeCartList.indices) {
                    if (beforeCartList[i].menuSeq == menuSeq) {
                        isChange = true
                        cartMenuInfo = CartMenuInfo(
                            menuSeq = menuSeq,
                            menuSize = beforeCartList[i].menuSize?.plus(menuSize!!),
                            menuTitle = menuTitle,
                            menuImgUrl =  menuImgUrl,
                            menuPrice = menuPrice
                        )
                        beforeCartList[i] = cartMenuInfo
                        break
                    } else {
                        isChange = false
                    }
                }

                if (isChange) {
                    afterMenu = beforeCartList
                } else {
                    afterMenu = gson.fromJson(beforeMenu, object : TypeToken<MutableList<CartMenuInfo>>() {}.type)
                    cartMenuInfo = CartMenuInfo(
                        menuSeq = menuSeq,
                        menuSize = menuSize,
                        menuTitle = menuTitle,
                        menuImgUrl =  menuImgUrl,
                        menuPrice = menuPrice
                    )
                    afterMenu.addAll(listOf(cartMenuInfo))
                }
            } else {
                afterMenu = mutableListOf()

                cartMenuInfo = CartMenuInfo(
                    menuSeq = menuSeq,
                    menuSize = menuSize,
                    menuTitle = menuTitle,
                    menuImgUrl =  menuImgUrl,
                    menuPrice = menuPrice
                )

                afterMenu.add(cartMenuInfo)
            }

            val reCartMenuInfo = gson.toJson(afterMenu)
            Log.d(TAG, "reCartMenuInfo : $reCartMenuInfo")
            appCache.putString(menuKey, reCartMenuInfo)
            appCache.commit()

            finish()
        }
    }
}