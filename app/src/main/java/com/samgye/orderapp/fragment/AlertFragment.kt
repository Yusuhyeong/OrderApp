package com.samgye.orderapp.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.samgye.orderapp.activity.viewmodel.AlertViewModel
import com.samgye.orderapp.databinding.FragmentCommonPopupBinding

class AlertFragment: DialogFragment() {
    private val TAG = this::class.java.simpleName
    private var _binding: FragmentCommonPopupBinding? = null
    private val binding get() = _binding!!

    private lateinit var popupViewModel: AlertViewModel
    private var mTitle = ""
    private var mMessage = ""
    private var mIsOneBtn = false
    private var mOnPositiveClick: (() -> Unit)? = null
    private var mOnNegativeClick: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        popupViewModel = ViewModelProvider(this)[AlertViewModel::class.java]
        _binding = FragmentCommonPopupBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.alertViewModel = popupViewModel

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        initViewModel()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initViewModel() {
        popupViewModel.apply {
            binding!!.alertViewModel = this

            setTitleText(mTitle)
            setMessageText(mMessage)
            setIsOneBtn(mIsOneBtn)

            confirm_event.observe(viewLifecycleOwner) {
                dismiss()
                mOnPositiveClick?.let { it() }
            }
            cancel_event.observe(viewLifecycleOwner) {
                dismiss()
                mOnNegativeClick?.let { it() }
            }
        }
    }

    fun setTitle(title: String): AlertFragment {
        if (title.isNotEmpty()) {
            mTitle = title
        }
        Log.d(TAG, title)
        return this
    }

    fun setMessage(message: String): AlertFragment {
        if (message.isNotEmpty()) {
            mMessage = message
        }
        Log.d(TAG, message)
        return this
    }

    fun setIsOneBtn(isOneBtn: Boolean): AlertFragment {
        mIsOneBtn = isOneBtn
        return this
    }

    fun setPositiveButton(onPositiveClick: (() -> Unit)?): AlertFragment {
        mOnPositiveClick = onPositiveClick
        return this
    }


    fun setNegativeButton(onNegativeClick: (() -> Unit)? = null): AlertFragment {
        mOnNegativeClick = onNegativeClick
        return this
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            super.show(manager, tag)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }
}