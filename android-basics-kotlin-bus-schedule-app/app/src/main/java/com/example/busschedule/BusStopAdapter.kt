package com.example.busschedule

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.busschedule.database.schedule.Schedule
import com.example.busschedule.databinding.BusStopItemBinding
import java.text.SimpleDateFormat
import java.util.*

class BusStopAdapter(private val onItemClicked: (Schedule) -> Unit) :
    ListAdapter<Schedule, BusStopAdapter.BusStopViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Schedule>() {
            /*
            * check if the id is the same
            */
            override fun areItemsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
                return oldItem.id == newItem.id
            }

            /*
            * Check if the content(not only the id) is the same
            */
            override fun areContentsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
                return oldItem == newItem
            }
        }
    }

    class BusStopViewHolder(private var binding: BusStopItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SimpleDateFormat")
        fun bind(schedule: Schedule) {
            // set text to stop name text view
            binding.stopNameTextView.text = schedule.stopName
            // set text using date format to arrival time
            binding.arrivalTimeTextView.text = SimpleDateFormat(
                "h:mm a"
            ).format(
                Date(schedule.arrivalTime.toLong() * 1000)
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusStopViewHolder {
        // create a view holder
        val viewHolder = BusStopViewHolder(
            BusStopItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

        // cal the onClickListener to call onItemClicked on current position
        viewHolder.itemView.setOnClickListener {
            //get the position
            val position = viewHolder.adapterPosition
            // call onItemClicked and add the item with current position
            onItemClicked(getItem(position))
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: BusStopViewHolder, position: Int) {
        // bind the view with current position data
        holder.bind(getItem(position))
    }
}