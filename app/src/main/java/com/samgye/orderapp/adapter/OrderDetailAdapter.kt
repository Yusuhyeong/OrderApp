package com.samgye.orderapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.samgye.orderapp.data.OrderDetailInfo
import com.samgye.orderapp.databinding.OrderDetailRvItemBinding

class OrderDetailAdapter() : ListAdapter<OrderDetailInfo, OrderDetailAdapter.OrderDetailViewHolder>(DiffCallback()) {
    class OrderDetailViewHolder(val binding: OrderDetailRvItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(orderDetailInfo: OrderDetailInfo) {
            binding.orderDetailItem = orderDetailInfo
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailViewHolder {
        val binding = OrderDetailRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderDetailViewHolder, position: Int) {
        val orderListItem = getItem(position)
        holder.bind(orderListItem)
    }

    class DiffCallback : DiffUtil.ItemCallback<OrderDetailInfo>() {
        override fun areItemsTheSame(oldItem: OrderDetailInfo, newItem: OrderDetailInfo): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: OrderDetailInfo, newItem: OrderDetailInfo): Boolean {
            return oldItem == newItem
        }
    }
}