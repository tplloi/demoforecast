package com.loitp.model.openweather

import androidx.annotation.Keep
import java.io.Serializable

@Keep
data class Temp(
        val day: Double = 0.0,
        val eve: Double = 0.0,
        val max: Double = 0.0,
        val min: Double = 0.0,
        val morn: Double = 0.0,
        val night: Double = 0.0
) : Serializable
