package com.example.weatherapp.models

// Represents the overall weather data containing current weather and forecast
data class Weather(
    val current: Current,
    val forecast: List<Forecast> // List of forecasted weather data for upcoming days
)

// Represents the current weather conditions
data class Current(
    val condition: String,
    val high: Int,
    val low: Int,
    val humidity: Int,
    val windSpeed: Int,
    val windDirection: String?,
    val precipitation: String?,
    val precipitationAmount: String?,
    val uvIndex: Int,
    val visibility: Int
)

// Represents the forecast data for upcoming days
data class Forecast(
    val day: String,
    val high: Int,
    val low: Int,
    val condition: String,
    val precipitation: String?,
    val precipitationAmount: String?,
    val windSpeed: Int,
    val windDirection: String?,
    val humidity: Int?
)
