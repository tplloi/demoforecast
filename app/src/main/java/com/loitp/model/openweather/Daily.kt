package com.loitp.model.openweather

import androidx.annotation.Keep
import java.io.Serializable

@Keep
data class Daily(
        val clouds: Int = 0,
        val dew_point: Double = 0.0,
        val dt: Long = 0,
        val feels_like: FeelsLike? = null,
        val humidity: Int = 0,
        val pop: Int = 0,
        val pressure: Int = 0,
        val sunrise: Long = 0,
        val sunset: Long = 0,
        val temp: Temp? = null,
        val uvi: Double = 0.0,
        val weather: List<Weather>,
        val wind_deg: Int = 0,
        val wind_speed: Double = 0.0
) : Serializable
