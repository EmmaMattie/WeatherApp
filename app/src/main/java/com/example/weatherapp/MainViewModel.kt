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

    // Holds the current weather data, starts as null (not loaded yet)
    private val _weather = MutableStateFlow<Weather?>(null)
    val weather: StateFlow<Weather?> = _weather  // Expose as read-only

    // Holds the location name string shown in UI
    private val _location = MutableStateFlow("Halifax, Nova Scotia")
    val location: StateFlow<String> = _location  // Read-only to outside

    // Retrofit setup to connect to the WeatherAPI service
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.weatherapi.com/")  // Base URL for API calls
        .addConverterFactory(GsonConverterFactory.create())  // Use Gson to convert JSON
        // Gson converts JSON from the API into Kotlin objects the app can use easily
        .build()

    // Create API service interface from Retrofit
    private val weatherService = retrofit.create(WeatherService::class.java)

    private val apiKey = "4d09ac90aef543f5a24121824252005"

    // Initialization block runs when ViewModel is created
    init {
        fetchWeather() // Fetch weather data for default location (Halifax)
    }

    // Function to fetch weather data for Halifax
    fun fetchWeather() {
        viewModelScope.launch {  // Launch coroutine to call API asynchronously
            try {
                val response = weatherService.getWeather(
                    apiKey = apiKey,
                    location = "Halifax",  // Hardcoded location string
                    days = 7,              // Request 7 days of forecast data
                    aqi = "no",            // No air quality info needed
                    alerts = "no"          // No weather alerts needed
                )
                _weather.value = response // Update the weather data state
            } catch (e: Exception) {
                e.printStackTrace()  // Print error if API call fails
            }
        }
    }

    // Function to fetch weather by latitude and longitude string
    fun fetchWeatherByLocation(location: String) {
        viewModelScope.launch {  // Starts a background task (a coroutine) that can run without freezing the app UI
            try {
                val response = weatherService.getWeather(
                    apiKey = apiKey,
                    location = location,
                    days = 7,
                    aqi = "no",
                    alerts = "no"
                )
                // This prints forecast details in the console to help check if the API data is correct.
                println("Forecast conditions for $location:")
                response.forecast.forecastday.forEach { day ->
                    println("${day}: ${day.day.condition.text}")
                }
                _weather.value = response  // Update weather state with API response
                println("Returned days: ${response.forecast.forecastday.size}")  // Debug: print number of forecast days

            } catch (e: Exception) {
                e.printStackTrace()  // Print error if call fails
            }
        }
    }

    // Update location string displayed in UI
    fun updateLocation(newLocation: String) {
        _location.value = newLocation
    }
}
