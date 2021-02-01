package com.loitp.model.openweather

import androidx.annotation.Keep
import java.io.Serializable

@Keep
data class OpenWeather(
        val daily: List<Daily>,
        val lat: Double = 0.0,
        val lon: Double = 0.0,
        val timezone: String = "",
        val timezone_offset: Int = 0
) : Serializable
