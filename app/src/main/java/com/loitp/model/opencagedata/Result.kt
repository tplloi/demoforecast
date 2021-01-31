package com.loitp.model.opencagedata

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class Result(

        @SerializedName("bounds")
        @Expose
        val bounds: Bounds? = null,

        @SerializedName("components")
        @Expose
        val components: Components? = null,

        @SerializedName("confidence")
        @Expose
        val confidence: Int = 0,

        @SerializedName("formatted")
        @Expose
        val formatted: String = "",

        @SerializedName("geometry")
        @Expose
        val geometry: Geometry? = null
)
