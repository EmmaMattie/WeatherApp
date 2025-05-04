package com.example.weatherapp.ui.screens

import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.weatherapp.MainViewModel

@Composable
fun CurrentWeather(viewModel: MainViewModel) {
    // Access the current weather data from the viewModel
    val weather = viewModel.currentWeather.value  // Accessing value of State

    // Column layout that takes up full screen size and adds padding
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally // Center items horizontally
    ) {
        // Display current weather condition
        Text("Current: ${weather.condition}")

        // Display high and low temperature
        Text("High: ${weather.high}°C")
        Text("Low: ${weather.low}°C")

        // Display humidity percentage
        Text("Humidity: ${weather.humidity}%")

        // Display wind direction, if none N/A
        Text("Wind Direction: ${weather.windDirection ?: "N/A"}")
    }
}
