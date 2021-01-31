package com.loitp.model.opencagedata

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Keep
data class Components(
        @SerializedName("_category")
        @Expose
        val _category: String = "",

        @SerializedName("_type")
        @Expose
        val _type: String = "",

        @SerializedName("city")
        @Expose
        val city: String = "",

        @SerializedName("city_district")
        @Expose
        val city_district: String = "",

        @SerializedName("continent")
        @Expose
        val continent: String = "",

        @SerializedName("country")
        @Expose
        val country: String = "",

        @SerializedName("country_code")
        @Expose
        val country_code: String = "",

        @SerializedName("county")
        @Expose
        val county: String = "",

        @SerializedName("hamlet")
        @Expose
        val hamlet: String = "",

        @SerializedName("municipality")
        @Expose
        val municipality: String = "",

        @SerializedName("neighbourhood")
        @Expose
        val neighbourhood: String = "",

        @SerializedName("postcode")
        @Expose
        val postcode: String = "",

        @SerializedName("quarter")
        @Expose
        val quarter: String = "",

        @SerializedName("region")
        @Expose
        val region: String = "",

        @SerializedName("residential")
        @Expose
        val residential: String = "",

        @SerializedName("road")
        @Expose
        val road: String = "",

        @SerializedName("road_type")
        @Expose
        val road_type: String = "",

        @SerializedName("state")
        @Expose
        val state: String = "",

        @SerializedName("state_code")
        @Expose
        val state_code: String = "",

        @SerializedName("state_district")
        @Expose
        val state_district: String = "",

        @SerializedName("town")
        @Expose
        val town: String = "",

        @SerializedName("village")
        @Expose
        val village: String = ""
) : Serializable
