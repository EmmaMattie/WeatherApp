package com.example.weatherapp.models

import com.google.gson.annotations.SerializedName

// Top-level weather response holding location, current weather, and forecast
data class Weather(
    val location: Location,          // Location information
    val current: CurrentWeather,     // Current weather details
    val forecast: Forecast           // Weather forecast data
)

// Location information like city, region, country, and local time
data class Location(
    val name: String,
    val region: String,
    val country: String,
    val localtime: String
)

// Current weather details with temperature, condition, wind, humidity, precipitation
data class CurrentWeather(
    // Links the JSON name 'temp_c' to the Kotlin property 'temperatureCelsius'
    @SerializedName("temp_c") val temperatureCelsius: Float,   // Current temperature in Celsius
    val condition: Condition,                                  // Weather condition text and icon
    @SerializedName("wind_kph") val windSpeedKph: Float,      // Wind speed in km/h
    @SerializedName("wind_dir") val windDirection: String,    // Wind direction text
    val humidity: Int,                                         // Humidity percentage
    @SerializedName("precip_mm") val precipitationMillimeters: Float // Precipitation in millimeters
)

// Weather condition description and icon URL
data class Condition(
    val text: String,    // Condition text
    val icon: String     // URL for weather icon
)

// Forecast containing a list of forecast days
data class Forecast(
    val forecastday: List<ForecastDay>  // List of daily forecast data
)

// Data for one day in the forecast
data class ForecastDay(
    val date: String,        // Date string e.g. 2025-10-15
    val day: DayDetails      // Weather details for the day
)

// Detailed weather info for a single day
data class DayDetails(
    // Matches JSON field to Kotlin property with a different name
    @SerializedName("maxtemp_c") val maxTemperatureCelsius: Float,    // Max temp in Celsius
    @SerializedName("mintemp_c") val minTemperatureCelsius: Float,    // Min temp in Celsius
    val condition: Condition,                                          // Condition text and icon
    @SerializedName("daily_chance_of_rain") val dailyChanceOfRain: Int, // Chance of rain percentage
    @SerializedName("totalprecip_mm") val totalPrecipitationMillimeters: Float, // Total precipitation mm
    @SerializedName("avghumidity") val averageHumidity: Float,         // Average humidity percentage
    @SerializedName("maxwind_kph") val maxWindSpeedKph: Float          // Max wind speed km/h
)
