package com.loitp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.annotation.LogTag
import com.core.adapter.BaseAdapter
import com.core.utilities.LDateUtil
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

        @SuppressLint("SetTextI18n")
        fun bind(daily: Daily) {
            itemView.tvDt.text = "Date: " + LDateUtil.getDateCurrentTimeZone(timestamp = daily.dt, format = "EEEE, dd-MMM-yyyy")
            itemView.tvSunrise.text = "Sunrise: " + LDateUtil.getDateCurrentTimeZone(timestamp = daily.sunrise, format = "HH:mm:ss")
            itemView.tvSunset.text = "Sunset: " + LDateUtil.getDateCurrentTimeZone(timestamp = daily.sunset, format = "HH:mm:ss")
            itemView.tvTempMax.text = "Max ${daily.temp?.max}°C"
            itemView.tvTempMin.text = "Max ${daily.temp?.min}°C"
            itemView.setOnClickListener {
                onClick?.invoke(daily)
            }
        }
    }
}
