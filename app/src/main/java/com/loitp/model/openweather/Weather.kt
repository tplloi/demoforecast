package com.loitp.model.openweather

import androidx.annotation.Keep
import java.io.Serializable

@Keep
data class Weather(
        val description: String = "",
        val icon: String = "",
        val id: Int = 0,
        val main: String = ""
) : Serializable
