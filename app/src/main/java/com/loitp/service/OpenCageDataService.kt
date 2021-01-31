package com.loitp.service

import com.loitp.model.opencagedata.OpenCageData
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenCageDataService {
    @GET("json/")
    fun getGeoCode(
            @Query("pretty") pretty: Int,
            @Query("no_annotations") noAnnotations: Int,
            @Query("no_ded upe") noDedUpe: Int,
            @Query("no_record") noRecord: Int,
            @Query("litmit") limit: Int,
            @Query("key") key: String,
            @Query("q") q: String
    ): Observable<OpenCageData>
}
