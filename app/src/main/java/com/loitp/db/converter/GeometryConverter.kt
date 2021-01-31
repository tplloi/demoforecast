package com.loitp.db.converter

import androidx.room.TypeConverter
import com.core.base.BaseApplication
import com.loitp.model.opencagedata.Components
import com.loitp.model.opencagedata.Geometry

class GeometryConverter {

    @TypeConverter
    fun fromGeometry(geometry: Geometry?): String? {
        if (geometry == null) {
            return null
        }
        return BaseApplication.gson.toJson(geometry)
    }

    @TypeConverter
    fun toGeometry(jsonString: String?): Geometry? {
        if (jsonString == null) {
            return null
        }
        return BaseApplication.gson.fromJson(jsonString, Geometry::class.java)
    }
}
