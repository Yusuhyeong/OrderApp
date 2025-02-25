package com.samgye.orderapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.samgye.orderapp.activity.viewmodel.OrderListViewModel
import com.samgye.orderapp.data.OrderListInfo
import com.samgye.orderapp.databinding.OrderListRvItemBinding

class OrderListAdapter(private val viewModel: OrderListViewModel) : ListAdapter<OrderListInfo, OrderListAdapter.OrderListViewHolder>(DiffCallback()) {
    class OrderListViewHolder(val binding: OrderListRvItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: OrderListViewModel, orderListItem: OrderListInfo) {
            binding.orderListViewModel = viewModel
            binding.orderListItem = orderListItem

            var menuTitle = StringBuilder()
            if (orderListItem.menuList != null) {
                for (i: Int in 0..<orderListItem.menuList.size) {
                    menuTitle.append("${orderListItem.menuList[i].menuTitle} X ${orderListItem.menuList[i].menuSize}, ")
                    binding.tvOrderType.text = orderListItem.orderStat
                }

                menuTitle.setLength(menuTitle.length - 2)
            } else {
                menuTitle.append("메뉴 정보 없음")
            }
            binding.tvMenuTitle.text = menuTitle
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderListViewHolder {
        val binding = OrderListRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderListViewHolder, position: Int) {
        val orderListItem = getItem(position)
        holder.bind(viewModel, orderListItem)
    }

    class DiffCallback : DiffUtil.ItemCallback<OrderListInfo>() {
        override fun areItemsTheSame(oldItem: OrderListInfo, newItem: OrderListInfo): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: OrderListInfo, newItem: OrderListInfo): Boolean {
            return oldItem == newItem
        }
    }
}