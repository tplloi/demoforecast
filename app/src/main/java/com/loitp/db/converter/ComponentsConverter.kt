package com.loitp.db.converter

import androidx.room.TypeConverter
import com.core.base.BaseApplication
import com.loitp.model.opencagedata.Bounds
import com.loitp.model.opencagedata.Components

class ComponentsConverter {

    @TypeConverter
    fun fromComponents(components: Components?): String? {
        if (components == null) {
            return null
        }
        return BaseApplication.gson.toJson(components)
    }

    @TypeConverter
    fun toComponents(jsonString: String?): Components? {
        if (jsonString == null) {
            return null
        }
        return BaseApplication.gson.fromJson(jsonString, Components::class.java)
    }
}
