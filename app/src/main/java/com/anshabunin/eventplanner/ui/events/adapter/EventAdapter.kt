package com.anshabunin.eventplanner.ui.events.adapter

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.anshabunin.eventplanner.R
import com.anshabunin.eventplanner.core.database.entity.EventEntity
import com.anshabunin.eventplanner.databinding.ItemEventBinding
import com.anshabunin.eventplanner.databinding.ViewEmptyListBinding

class EventAdapter(
    private val eventsListener: EventsListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var eventsList = mutableListOf<EventEntity>()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is EventsViewHolder) holder.bind(eventsList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == R.layout.item_event) {
            EventsViewHolder(
                ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        } else {
            EmptyViewHolder(
                ViewEmptyListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    inner class EventsViewHolder(private val binding: ItemEventBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: EventEntity?) {
            event?.let { event ->
                binding.apply {
                    eventTitle.text = event.title
                    eventTown.text = event.city
                    eventDateTime.text = event.date
                }

                binding.root.setOnClickListener {
                    Log.e("ERRROR", "Ты нажал на на ${event.title}")
                    eventsListener.openEventDetail(event.id)
                }
            }
        }
    }

    class EmptyViewHolder(binding: ViewEmptyListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        return if (eventsList.isEmpty()) R.layout.view_empty_list
        else R.layout.item_event
    }

    override fun getItemCount(): Int {
        return if (eventsList.isEmpty()) 0 else eventsList.size
    }

    fun updateEvents(newEventList: List<EventEntity>) {
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize() = eventsList.size

            override fun getNewListSize() = newEventList.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return diffUtil.areItemsTheSame(eventsList[oldItemPosition], newEventList[newItemPosition])
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return diffUtil.areContentsTheSame(eventsList[oldItemPosition], newEventList[newItemPosition])
            }
        })

        eventsList = newEventList.toMutableList()
        diffResult.dispatchUpdatesTo(this)
    }


    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<EventEntity>() {
            override fun areItemsTheSame(oldItem: EventEntity, newItem: EventEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: EventEntity, newItem: EventEntity): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}