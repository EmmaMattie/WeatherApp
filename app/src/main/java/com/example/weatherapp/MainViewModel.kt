package com.example.weatherapp

import androidx.lifecycle.ViewModel
import com.example.weatherapp.models.Current
import com.example.weatherapp.models.Forecast
import com.example.weatherapp.models.Weather
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State

// ViewModel class for managing weather-related data
class MainViewModel : ViewModel() {

    // Weather variable initialized inside the init block
    private val _currentWeather = mutableStateOf(
        Current(
            "Cloudy",  // condition
            14,         // high temperature
            10,         // low temperature
            65,         // humidity
            15,         // wind speed
            "North",    // wind direction
            "Rain",     // precipitation type
            "5 mm",     // precipitation amount
            6,          // UV index
            10          // visibility
        )
    )

    // Exposes currentWeather as a read-only State
    val currentWeather: State<Current> = _currentWeather

    // Variable to hold overall weather data, including forecast
    val weatherData: Weather

    // Initialization block to set up forecast and weather data
    init {
        // List of forecasted weather for the upcoming days
        val forecast = listOf(
            Forecast("Monday", 3, 0, "Rainy", "Rain", "10 mm", 10, "East", 75),
            Forecast("Tuesday", 4, 2, "Sunny", null, null, 5, "South", 65),
            Forecast("Wednesday", 6, 3, "Cloudy", null, null, 7, "West", 60),
            Forecast("Thursday", 8, 5, "Rainy", "Rain", "15 mm", 12, "North", 55)
        )

        // Set the weatherData variable to hold current weather and forecast
        weatherData = Weather(_currentWeather.value, forecast)
    }
}
