package com.loitp.db.converter

import androidx.room.TypeConverter
import com.core.base.BaseApplication
import com.loitp.model.opencagedata.Bounds

class BoundsConverter {

    @TypeConverter
    fun fromBounds(bounds: Bounds?): String? {
        if (bounds == null) {
            return null
        }
        return BaseApplication.gson.toJson(bounds)
    }

    @TypeConverter
    fun toBounds(jsonString: String?): Bounds? {
        if (jsonString == null) {
            return null
        }
        return BaseApplication.gson.fromJson(jsonString, Bounds::class.java)
    }
}
