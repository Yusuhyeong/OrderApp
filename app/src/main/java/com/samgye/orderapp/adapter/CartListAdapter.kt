package com.samgye.orderapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.samgye.orderapp.activity.viewmodel.CartViewModel
import com.samgye.orderapp.data.CartMenuInfo
import com.samgye.orderapp.databinding.CartListRvItemBinding

class CartListAdapter(private val viewModel: CartViewModel) : ListAdapter<CartMenuInfo, CartListAdapter.CartListViewHolder>(DiffCallback()) {
    class CartListViewHolder(val binding: CartListRvItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: CartViewModel, cartMenuInfo: CartMenuInfo) {
            binding.cartViewModel = viewModel
            binding.cartItem = cartMenuInfo
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartListViewHolder {
        val binding = CartListRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartListViewHolder, position: Int) {
        val cartItem = getItem(position)
        holder.bind(viewModel, cartItem)
    }

    class DiffCallback : DiffUtil.ItemCallback<CartMenuInfo>() {
        override fun areItemsTheSame(oldItem: CartMenuInfo, newItem: CartMenuInfo): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CartMenuInfo, newItem: CartMenuInfo): Boolean {
            return oldItem == newItem
        }
    }
}