package com.example.weatherapp.models

import com.google.gson.annotations.SerializedName

// Main weather data object returned by the API, containing location, current conditions, and forecast
data class Weather(
    val location: Location,          // Location information (city)
    val current: CurrentWeather,     // Current weather details
    val forecast: Forecast           // Weather forecast data
)

// Location information like city and local time
data class Location(
    val name: String,                // City name
    val region: String,              // Region or state
    val country: String,             // Country name
    val localtime: String            // Local date and time as string
)

// Current weather details with temperature, condition, wind, humidity, precipitation
data class CurrentWeather(
    // If JSON has "temp_c", use its value here as temperatureCelsius
    @SerializedName("temp_c") val temperatureCelsius: Float,   // Current temperature in Celsius
    val condition: Condition,                                  // Weather condition text and icon
    @SerializedName("wind_kph") val windSpeedKph: Float,      // Wind speed in km/h
    @SerializedName("wind_dir") val windDirection: String,    // Wind direction (N, NE, WSW, etc.)
    val humidity: Int,                                         // Humidity percentage (0-100)
    @SerializedName("precip_mm") val precipitationMillimeters: Float // Precipitation amount in millimeters
)

// Weather condition description and icon URL
data class Condition(
    val text: String,    // Text description of the weather
    val icon: String     // URL to the weather icon image
)

// Forecast containing a list of forecast days
data class Forecast(
    val forecastday: List<ForecastDay>  // List of daily forecast data
)

// Data for one day in the forecast
data class ForecastDay(
    val date: String,        // Date of forecast day
    val day: DayDetails      // Weather details for that day
)

// Detailed weather info for a single day
data class DayDetails(
    // If JSON has "maxtemp_c", use its value here as maxTemperatureCelsius
    @SerializedName("maxtemp_c") val maxTemperatureCelsius: Float,    // Max temperature for the day in Celsius
    @SerializedName("mintemp_c") val minTemperatureCelsius: Float,    // Min temperature for the day in Celsius
    val condition: Condition,                                          // Weather condition text and icon for the day
    @SerializedName("daily_chance_of_rain") val dailyChanceOfRain: Int, // Chance of rain percentage (0-100)
    @SerializedName("totalprecip_mm") val totalPrecipitationMillimeters: Float, // Total precipitation in mm
    @SerializedName("avghumidity") val averageHumidity: Float,         // Average humidity percentage (0-100)
    @SerializedName("maxwind_kph") val maxWindSpeedKph: Float          // Max wind speed in km/h
)
