package com.samgye.orderapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.samgye.orderapp.activity.viewmodel.MenuViewModel
import com.samgye.orderapp.data.MenuInfo
import com.samgye.orderapp.databinding.MenuListRvItemBinding
import com.samgye.orderapp.utils.Constants

class MenuListAdapter(private val viewModel: MenuViewModel) : ListAdapter<MenuInfo, MenuListAdapter.MenuListViewHolder>(DiffCallback()) {
    class MenuListViewHolder(val binding: MenuListRvItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: MenuViewModel, menuInfo: MenuInfo) {
            binding.menuViewModel = viewModel
            binding.menuItem = menuInfo
            if (menuInfo.menuImgUrl != null) {
                val imgUrl = Constants.IMG_BASE_URL + menuInfo.menuImgUrl
                Glide.with(binding.ivMenuImg.context)
                    .load(imgUrl)
                    .into(binding.ivMenuImg)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuListViewHolder {
        val binding = MenuListRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuListViewHolder, position: Int) {
        val menuItem = getItem(position)
        holder.bind(viewModel, menuItem)
    }

    class DiffCallback : DiffUtil.ItemCallback<MenuInfo>() {
        override fun areItemsTheSame(oldItem: MenuInfo, newItem: MenuInfo): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MenuInfo, newItem: MenuInfo): Boolean {
            return oldItem == newItem
        }
    }
}