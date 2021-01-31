package com.loitp.model.opencagedata

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.loitp.db.converter.BoundsConverter
import com.loitp.db.converter.ComponentsConverter
import com.loitp.db.converter.GeometryConverter

@Keep
@Entity(tableName = "Result")
@TypeConverters(
        BoundsConverter::class,
        ComponentsConverter::class,
        GeometryConverter::class
)
data class Result(

        @ColumnInfo(name = "bounds")
        @SerializedName("bounds")
        @Expose
        var bounds: Bounds? = null,

        @ColumnInfo(name = "components")
        @SerializedName("components")
        @Expose
        var components: Components? = null,

        @ColumnInfo(name = "confidence")
        @SerializedName("confidence")
        @Expose
        var confidence: Int = 0,

        @PrimaryKey
        @ColumnInfo(name = "formatted")
        @SerializedName("formatted")
        @Expose
        var formatted: String = "",

        @ColumnInfo(name = "geometry")
        @SerializedName("geometry")
        @Expose
        var geometry: Geometry? = null
)
