package com.samgye.orderapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.samgye.orderapp.activity.viewmodel.HomeViewModel
import com.samgye.orderapp.data.EventInfo
import com.samgye.orderapp.databinding.EventListVpItemBinding

class EventListAdapter(private val viewModel: HomeViewModel, private val context: Context) : ListAdapter<EventInfo, EventListAdapter.EventViewHolder>(DiffCallback()) {
    class EventViewHolder(val binding: EventListVpItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: HomeViewModel, eventInfo: EventInfo, context: Context) {
            binding.homeViewModel = viewModel
            binding.eventItem = eventInfo
            Glide.with(context).load(eventInfo.eventImg).into(binding.ivEvent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = EventListVpItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val eventItem = getItem(position)
        holder.bind(viewModel, eventItem, context)
    }

    class DiffCallback : DiffUtil.ItemCallback<EventInfo>() {
        override fun areItemsTheSame(oldItem: EventInfo, newItem: EventInfo): Boolean {
            return oldItem.eventSeq == newItem.eventSeq
        }

        override fun areContentsTheSame(oldItem: EventInfo, newItem: EventInfo): Boolean {
            return oldItem == newItem
        }
    }
}