package com.loitp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.loitp.db.converter.BoundsConverter
import com.loitp.db.converter.ComponentsConverter
import com.loitp.db.converter.GeometryConverter
import com.loitp.model.opencagedata.Result

@Database(
        entities = [Result::class],
        version = 2
)
abstract class AppDatabase : RoomDatabase() {

    @TypeConverters(
            BoundsConverter::class,
            ComponentsConverter::class,
            GeometryConverter::class
    )

    abstract fun openCageDataDao(): OpenCageDataDao

    companion object {

        var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (instance == null) {
                synchronized(AppDatabase::class) {
                    instance = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "AppDatabase")
                            .fallbackToDestructiveMigration()
                            .build()
                }
            }

            return instance
        }

    }
}
