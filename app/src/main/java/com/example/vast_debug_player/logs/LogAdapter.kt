package com.example.vast_debug_player.logs

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vast_debug_player.databinding.ItemLogBinding

class LogAdapter : RecyclerView.Adapter<LogAdapter.LogViewHolder>() {

    private val items = mutableListOf<AdLogItem>()

    inner class LogViewHolder(val binding: ItemLogBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        val binding = ItemLogBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        val item = items[position]
        holder.binding.logText.text = "[${item.time}] ${item.message}"

        holder.binding.logText.setTextColor(
            when (item.type) {
                LogType.ERROR -> Color.RED
                LogType.QUARTILE -> Color.CYAN
                LogType.LIFECYCLE -> Color.GREEN
                LogType.USER_ACTION -> Color.YELLOW
                LogType.DEFAULT -> Color.WHITE
            }
        )
    }

    override fun getItemCount() = items.size

    fun add(item: AdLogItem) {
        items.add(item)
        notifyItemInserted(items.lastIndex)
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }
}