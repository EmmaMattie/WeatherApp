package com.example.weatherapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.models.Weather
import com.example.weatherapp.services.WeatherService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// ViewModel handles weather data and location state
class MainViewModel : ViewModel() {

    // StateFlow to hold the current weather data
    private val _weather = MutableStateFlow<Weather?>(null)
    val weather: StateFlow<Weather?> = _weather

    // New StateFlow to hold the current location string
    private val _location = MutableStateFlow("Halifax, Nova Scotia") // default location shown initially
    val location: StateFlow<String> = _location

    // Retrofit instance to call the Weather API
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.weatherapi.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Create service interface from Retrofit
    private val weatherService = retrofit.create(WeatherService::class.java)

    private val apiKey = "4d09ac90aef543f5a24121824252005"

    init {
        fetchWeather() // Initial fetch using default location
    }

    // Updates the current location string to show on UI
    fun updateLocation(newLocation: String) {
        _location.value = newLocation
    }

    // Fetches weather for default location
    fun fetchWeather() {
        viewModelScope.launch {
            try {
                val response = weatherService.getWeather(
                    apiKey = apiKey,
                    location = "Halifax",
                    days = 7,
                    aqi = "no",
                    alerts = "no"
                )
                _weather.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Fetches weather based on given location string (lat,lng or city)
    fun fetchWeatherByLocation(location: String) {
        viewModelScope.launch {
            try {
                val response = weatherService.getWeather(
                    apiKey = apiKey,
                    location = location,
                    days = 7,
                    aqi = "no",
                    alerts = "no"
                )
                _weather.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
