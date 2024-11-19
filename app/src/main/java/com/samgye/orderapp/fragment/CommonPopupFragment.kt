package com.samgye.orderapp.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.samgye.orderapp.activity.viewmodel.PopupViewModel
import com.samgye.orderapp.data.PopupData
import com.samgye.orderapp.databinding.FragmentCommonPopupBinding

class CommonPopupFragment(data: PopupData, viewModel: PopupViewModel) : DialogFragment() {
    private val TAG = this::class.java.simpleName
    private lateinit var binding: FragmentCommonPopupBinding
    private val popupData = data
    private val popupViewModel = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        binding = FragmentCommonPopupBinding.inflate(layoutInflater, container, false)
        binding.popupViewModel = popupViewModel
        binding.lifecycleOwner = this

        popupViewModel.loadPopupData(popupData)

        binding.tvPopupCancel.setOnClickListener {
            popupViewModel.popupEvent.value = "cancel"
            dismiss()
        }

        binding.tvPopupConfirm.setOnClickListener {
            popupViewModel.popupEvent.value = "confirm"
            dismiss()
        }

        return binding.root
    }
}