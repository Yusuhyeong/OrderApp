package com.samgye.orderapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.samgye.orderapp.activity.viewmodel.MenuViewModel
import com.samgye.orderapp.data.CategoryInfo
import com.samgye.orderapp.databinding.CategoryListRvItemBinding

class CategoryListAdapter(private val viewModel: MenuViewModel) : ListAdapter<CategoryInfo, CategoryListAdapter.CategoryListViewHolder>(DiffCallback()) {
    class CategoryListViewHolder(val binding: CategoryListRvItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: MenuViewModel, categoryItem: CategoryInfo) {
            binding.menuViewModel = viewModel
            binding.categoryItem = categoryItem
            val menuListAdapter = MenuListAdapter(viewModel)
            binding.rvMenuList.adapter = menuListAdapter

            menuListAdapter.submitList(categoryItem.menu)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryListViewHolder {
        val binding = CategoryListRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryListViewHolder, position: Int) {
        val categoryItem = getItem(position)
        holder.bind(viewModel, categoryItem)
    }

    class DiffCallback : DiffUtil.ItemCallback<CategoryInfo>() {
        override fun areItemsTheSame(oldItem: CategoryInfo, newItem: CategoryInfo): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CategoryInfo, newItem: CategoryInfo): Boolean {
            return oldItem == newItem
        }
    }
}