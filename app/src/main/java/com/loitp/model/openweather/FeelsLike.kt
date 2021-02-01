package com.loitp.model.openweather

import androidx.annotation.Keep
import java.io.Serializable

@Keep
data class FeelsLike(
        val day: Double = 0.0,
        val eve: Double = 0.0,
        val morn: Double = 0.0,
        val night: Double = 0.0
) : Serializable
