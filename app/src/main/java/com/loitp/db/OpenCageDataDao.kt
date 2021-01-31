package com.loitp.db

import androidx.room.Dao
import androidx.room.Query
import com.core.base.BaseDao

@Dao
interface OpenCageDataDao : BaseDao<com.loitp.model.opencagedata.Result> {

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertListResult(list: List<com.loitp.model.opencagedata.Result>)

    @Query("SELECT * FROM Result")
    fun getListResult(): List<com.loitp.model.opencagedata.Result>

    @Query("SELECT * FROM Result WHERE formatted LIKE '%' || :formatted || '%'")
    fun getListResult(formatted: String): List<com.loitp.model.opencagedata.Result>

//    @Query("DELETE FROM Comic")
//    suspend fun deleteAll()

//    @Query("SELECT * FROM Comic WHERE url=:url")
//    fun find(url: String): Comic?
}
