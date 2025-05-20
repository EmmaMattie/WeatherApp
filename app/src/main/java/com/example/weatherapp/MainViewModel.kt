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

class MainViewModel : ViewModel() {

    // Holds weather data (null if not loaded)
    private val _weather = MutableStateFlow<Weather?>(null)
    val weather: StateFlow<Weather?> = _weather

    // Holds location name for UI display
    private val _location = MutableStateFlow("Loading...")
    val location: StateFlow<String> = _location

    // Setup Retrofit for API calls
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.weatherapi.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Create service from interface
    private val weatherService = retrofit.create(WeatherService::class.java)

    // Your API key for WeatherAPI
    private val apiKey = "4d09ac90aef543f5a24121824252005"

    // Called when ViewModel is created
    init {
        fetchWeather() // Fetch default weather (Halifax)
    }

    // Fetch weather for Halifax
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
                _weather.value = response // Update weather data
            } catch (e: Exception) {
                e.printStackTrace() // Log error if call fails
            }
        }
    }

    // Fetch weather by given location (lat,lng)
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
                _weather.value = response // Update weather data
            } catch (e: Exception) {
                e.printStackTrace() // Log error
            }
        }
    }

    // Change location shown in UI
    fun updateLocation(newLocation: String) {
        _location.value = newLocation
    }
}
