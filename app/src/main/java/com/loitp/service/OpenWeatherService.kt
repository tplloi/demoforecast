package com.loitp.service

import com.loitp.model.openweather.OpenWeather
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherService {
    @GET("onecall")
    fun getWeather(
            @Query("lat") lat: Double,
            @Query("lon") lon: Double,
            @Query("exclude") exclude: String,
            @Query("appid") appid: String
    ): Observable<OpenWeather>
}
