package com.loitp.model.opencagedata

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class Geometry(

        @SerializedName("lat")
        @Expose
        val lat: Double = 0.0,

        @SerializedName("lng")
        @Expose
        val lng: Double = 0.0
)