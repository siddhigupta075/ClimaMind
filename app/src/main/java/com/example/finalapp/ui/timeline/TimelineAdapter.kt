package com.example.finalapp.ui.timeline

import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.finalapp.R
import com.example.finalapp.data.remote.model.ForecastItem
import com.example.finalapp.utils.SuggestionEngine
import kotlin.math.abs

class TimelineAdapter(
    private val list: List<ForecastItem>,
    private val userType: String
) : RecyclerView.Adapter<TimelineAdapter.ViewHolder>() {

    private val bestTemp = 25.0

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val time: TextView = view.findViewById(R.id.timeText)
        val temp: TextView = view.findViewById(R.id.tempText)
        val condition: TextView = view.findViewById(R.id.conditionText)
        val suggestion: TextView = view.findViewById(R.id.suggestionText)
        val tag: TextView = view.findViewById(R.id.tagText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_timeline, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = list[position]

        val temp = item.main.temp
        val hour = item.dt_txt.substring(11, 13).toInt()

        // Convert to AM/PM
        val displayTime = when {
            hour == 0 -> "12 AM"
            hour < 12 -> "$hour AM"
            hour == 12 -> "12 PM"
            else -> "${hour - 12} PM"
        }

        holder.time.text = displayTime
        holder.temp.text = "${temp.toInt()}°C"

        val condition = item.weather.firstOrNull()?.main ?: "Clear"
        holder.condition.text = condition

        val suggestion = SuggestionEngine.getSuggestion(temp, hour, userType)
        holder.suggestion.text = suggestion

        // 🔥 SMART TAGGING SYSTEM
        when {
            abs(temp - 25) < 2 -> {
                holder.tag.text = "BEST"
                holder.tag.setTextColor(0xFF22C55E.toInt())
            }

            temp > 35 -> {
                holder.tag.text = "AVOID"
                holder.tag.setTextColor(0xFFEF4444.toInt())
            }

            temp < 15 -> {
                holder.tag.text = "COLD"
                holder.tag.setTextColor(0xFF38BDF8.toInt())
            }

            else -> {
                holder.tag.text = ""
            }
        }
    }
}