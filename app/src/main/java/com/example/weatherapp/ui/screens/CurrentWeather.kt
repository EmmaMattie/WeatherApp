package com.example.weatherapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R
import com.example.weatherapp.models.Weather

@Composable
fun CurrentWeatherScreen(weather: Weather?) {
    if (weather == null) {
        // Show loading text if data is not ready yet
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Loading...", fontSize = 20.sp, color = Color.White)
        }
        return
    }

    val conditionText = weather.current.condition.text

    // Pick background image based on weather condition text
    // Pick background image based on weather condition text
    val backgroundImage = when {
        conditionText.contains("Thunder", ignoreCase = true) || conditionText.contains("Storm", ignoreCase = true) -> R.drawable.thunderstorm_background
        conditionText.contains("Snow", ignoreCase = true) -> R.drawable.snow_background
        conditionText.contains("Rain", ignoreCase = true) -> R.drawable.rainy_background
        conditionText.contains("Fog", ignoreCase = true) || conditionText.contains("Mist", ignoreCase = true) -> R.drawable.fog_background
        conditionText.contains("Cloud", ignoreCase = true) || conditionText.contains("Overcast", ignoreCase = true) -> R.drawable.cloudy_background
        conditionText.contains("Sunny", ignoreCase = true) -> R.drawable.sunny_background
        else -> R.drawable.sunny_background // Default background
    }

    // Pick icon image based on weather condition text
    val iconId = when {
        conditionText.contains("Sunny", ignoreCase = true) -> R.drawable.sunny
        conditionText.contains("Cloud", ignoreCase = true) || conditionText.contains("Overcast", ignoreCase = true) -> R.drawable.cloudy
        conditionText.contains("Rain", ignoreCase = true) -> R.drawable.rainy
        conditionText.contains("Snow", ignoreCase = true) -> R.drawable.snow
        conditionText.contains("Fog", ignoreCase = true) || conditionText.contains("Mist", ignoreCase = true) -> R.drawable.fog
        conditionText.contains("Thunder", ignoreCase = true) -> R.drawable.thunderstorm
        else -> R.drawable.sunny // Default icon
    }

    Box(Modifier.fillMaxSize()) {
        // Set the background image filling the whole screen
        Image(
            painter = painterResource(id = backgroundImage),
            contentDescription = null, // contentDescription is null because this image is just for decoration and doesn't need a description for screen readers
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Crop image to fill the space
        )

        // Main scrollable column with weather content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Weather icon image
            Image(
                painter = painterResource(id = iconId),
                contentDescription = "Weather Icon",
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Location name
            Text(
                "Halifax",
                fontSize = 28.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            // Current temperature
            Text(
                "${weather.current.temperatureCelsius}°C",
                fontSize = 44.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            // Condition text
            Text(
                conditionText,
                fontSize = 22.sp,
                color = Color.White,
                fontWeight = FontWeight.Medium
            )

            // High and Low temperatures
            Text(
                "H: ${weather.forecast.forecastday[0].day.maxTemperatureCelsius}°  L: ${weather.forecast.forecastday[0].day.minTemperatureCelsius}°",
                fontSize = 22.sp,
                color = Color.White,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Individual cards for each weather metric
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                WeatherInfoCard("Wind Direction", weather.current.windDirection)
                WeatherInfoCard("Wind Speed", "${weather.current.windSpeedKph} km/h")
                WeatherInfoCard("Humidity", "${weather.current.humidity}%")
                WeatherInfoCard("Precipitation", "${weather.current.precipitationMillimeters} mm")
                WeatherInfoCard(
                    "Chance of Precipitation",
                    "${weather.forecast.forecastday[0].day.dailyChanceOfRain}%"
                )
            }
        }
    }
}

// Composable function to display a styled info box with label and value
@Composable
fun WeatherInfoCard(label: String, value: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = Color(0xAA444444), // semi-transparent gray
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(label, fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
            Text(value, fontSize = 16.sp, color = Color.White)
        }
    }
}
