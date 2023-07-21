package com.example.weatherapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("weather")
    fun getWeatherByLocation(
        @Query("q") location: String,
        @Query("appid") apiKey: String): Call<WeatherData>
}
