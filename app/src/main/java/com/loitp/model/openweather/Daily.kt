package com.loitp.model.openweather

import androidx.annotation.Keep
import java.io.Serializable

@Keep
data class Daily(
        val clouds: Double = 0.0,
        val dew_point: Double = 0.0,
        val dt: Long = 0,
        val feels_like: FeelsLike? = null,
        val humidity: Double = 0.0,
        val pop: Double = 0.0,
        val pressure: Double = 0.0,
        val sunrise: Long = 0,
        val sunset: Long = 0,
        val temp: Temp? = null,
        val uvi: Double = 0.0,
        val weather: List<Weather>,
        val wind_deg: Double = 0.0,
        val wind_speed: Double = 0.0
) : Serializable {
    fun getIcon(): String {
        return try {
            val icon = weather[0].icon
            "http://openweathermap.org/img/wn/${icon}@2x.png"
        } catch (e: Exception) {
            ""
        }
    }
}
