package com.loitp.model.opencagedata

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Keep
data class Rate(

        @SerializedName("limit")
        @Expose
        val limit: Int = 0,

        @SerializedName("remaining")
        @Expose
        val remaining: Int = 0,

        @SerializedName("reset")
        @Expose
        val reset: Int = 0
) : Serializable
