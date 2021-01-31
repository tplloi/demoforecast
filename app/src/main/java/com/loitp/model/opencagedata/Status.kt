package com.loitp.model.opencagedata

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Keep
data class Status(

        @SerializedName("code")
        @Expose
        val code: Int = 0,

        @SerializedName("message")
        @Expose
        val message: String = ""
) : Serializable
