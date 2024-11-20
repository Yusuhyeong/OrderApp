package com.samgye.orderapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.samgye.orderapp.activity.viewmodel.NoticeViewModel
import com.samgye.orderapp.data.NoticeItem
import com.samgye.orderapp.databinding.NoticeRvItemBinding

class NoticeListAdapter(private val viewModel: NoticeViewModel) : ListAdapter<NoticeItem, NoticeListAdapter.NoticeViewHolder>(DiffCallback()) {
    class NoticeViewHolder(val binding: NoticeRvItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: NoticeViewModel, noticeItem: NoticeItem) {
            binding.noticeViewModel = viewModel
            binding.noticeItem = noticeItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeViewHolder {
        val binding = NoticeRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoticeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoticeViewHolder, position: Int) {
        val noticeList = getItem(position)
        holder.bind(viewModel, noticeList)
    }

    class DiffCallback : DiffUtil.ItemCallback<NoticeItem>() {
        override fun areItemsTheSame(oldItem: NoticeItem, newItem: NoticeItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: NoticeItem, newItem: NoticeItem): Boolean {
            return oldItem == newItem
        }
    }
}