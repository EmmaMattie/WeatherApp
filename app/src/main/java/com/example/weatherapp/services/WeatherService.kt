package com.example.weatherapp.services

import com.example.weatherapp.models.Weather

// Interface for weather data fetching
interface WeatherService {
    fun getCurrentWeather(): Weather // Get current weather
    fun getForecast(): List<Weather> // Get weather forecast
}
