package com.example.weatherapp.services

import com.example.weatherapp.models.Weather
import retrofit2.http.GET
import retrofit2.http.Query

// Retrofit interface to call the Weather API
interface WeatherService {

    // GET request to fetch weather forecast data
    // Uses query parameters for API key, location, days, air quality, and alerts
    @GET("v1/forecast.json")
    suspend fun getWeather(
        @Query("key") apiKey: String,       // API authentication key
        @Query("q") location: String,       // Location query (city or coordinates)
        @Query("days") days: Int,            // Number of forecast days
        @Query("aqi") aqi: String,           // Air quality info ("yes" or "no")
        @Query("alerts") alerts: String      // Weather alerts ("yes" or "no")
    ): Weather // Returns Weather data class parsed from JSON
}
