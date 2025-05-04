package com.example.weatherapp.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.weatherapp.MainViewModel

@Composable
fun DailyForecastScreen(viewModel: MainViewModel) {
    // Column layout with padding around the content
    Column(modifier = Modifier.padding(16.dp)) {

        // Loop through each day's forecast data from the viewModel
        for (weather in viewModel.weatherData.forecast) {

            // Display the weather condition, high and low temperatures
            Text(text = "${weather.condition} - High: ${weather.high}°, Low: ${weather.low}°")

            // Display precipitation type (or "N/A" if not available)
            Text(text = "Precipitation: ${weather.precipitation ?: "N/A"}")

            // Display precipitation amount (or "N/A" if not available)
            Text(text = "Precipitation Amount: ${weather.precipitationAmount ?: "N/A"}")

            // Display wind speed and direction (or "N/A" if not available)
            Text(text = "Wind: ${weather.windSpeed} km/h, Direction: ${weather.windDirection ?: "N/A"}")

            // Display humidity percentage
            Text(text = "Humidity: ${weather.humidity} %")

            // Add space between each day's forecast
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
