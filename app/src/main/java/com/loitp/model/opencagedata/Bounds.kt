package com.loitp.model.opencagedata

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class Bounds(
        @SerializedName("northeast")
        @Expose
        val northeast: Northeast? = null,

        @SerializedName("southwest")
        @Expose
        val southwest: Southwest? = null
)
