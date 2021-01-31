package com.loitp.model.opencagedata

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class OpenCageData(

        @SerializedName("rate")
        @Expose
        val rate: Rate? = null,

        @SerializedName("results")
        @Expose
        val results: List<Result>,

        @SerializedName("status")
        @Expose
        val status: Status? = null
)