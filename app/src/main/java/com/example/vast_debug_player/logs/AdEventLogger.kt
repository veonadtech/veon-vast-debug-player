package com.example.vast_debug_player.logs

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class AdEventLogger(
    private val recyclerView: RecyclerView
) {

    private val adapter = LogAdapter()
    private val formatter = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)

    init {
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.adapter = adapter
    }

    fun log(message: String) {
        val time = formatter.format(Date())
        val type = detectType(message)

        adapter.add(
            AdLogItem(
                time = time,
                message = message,
                type = type
            )
        )

        recyclerView.scrollToPosition(adapter.itemCount - 1)
    }

    fun clear() {
        adapter.clear()
    }

    private fun detectType(message: String): LogType {
        val lower = message.lowercase()

        return when {
            lower.contains("error") -> LogType.ERROR

            lower.contains("firstquartile") ||
                    lower.contains("midpoint") ||
                    lower.contains("thirdquartile") -> LogType.QUARTILE

            lower.contains("impression") ||
                    lower.contains("start") ||
                    lower.contains("complete") -> LogType.LIFECYCLE

            lower.contains("pause") ||
                    lower.contains("resume") ||
                    lower.contains("click") ||
                    lower.contains("skip") ||
                    lower.contains("mute") ||
                    lower.contains("unmute") -> LogType.USER_ACTION

            else -> LogType.DEFAULT
        }
    }
}