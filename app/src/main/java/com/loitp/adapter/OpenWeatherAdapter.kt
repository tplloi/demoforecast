package com.loitp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.annotation.LogTag
import com.core.adapter.BaseAdapter
import com.loitp.R
import com.loitp.model.openweather.Daily
import kotlinx.android.synthetic.main.view_row_open_weather.view.*
import java.util.*

@LogTag("OpenWeatherAdapter")
class OpenWeatherAdapter(
        private val onClick: ((Daily) -> Unit)? = null
) : BaseAdapter() {

    private val listDaily = ArrayList<Daily>()

    fun setItems(listDaily: List<Daily>) {
        this.listDaily.clear()
        this.listDaily.addAll(listDaily)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        val itemView =
                LayoutInflater.from(parent.context).inflate(R.layout.view_row_open_weather, parent, false)
        return DailyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DailyViewHolder) {
            holder.bind(listDaily[position])
        }
    }

    override fun getItemCount(): Int {
        return listDaily.size
    }

    inner class DailyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(daily: Daily) {
            itemView.tvDate.text = "${daily.dt}"
            itemView.setOnClickListener {
                onClick?.invoke(daily)
            }
        }
    }
}
