package com.example.weatherapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
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
fun DailyForecastScreen(weather: Weather?) {
    // Show loading text if weather data is not yet available
    if (weather == null) {
        Text("Loading...", modifier = Modifier.fillMaxSize(), color = Color.White)
        return
    }

    // Use the current weather condition to decide the background image
    val currentConditionText = weather.current.condition.text
    val backgroundImage = when {
        currentConditionText.contains("Sunny", ignoreCase = true) -> R.drawable.sunny_background
        currentConditionText.contains("Cloud", ignoreCase = true) -> R.drawable.cloudy_background
        currentConditionText.contains("Rain", ignoreCase = true) -> R.drawable.rainy_background
        currentConditionText.contains("Snow", ignoreCase = true) -> R.drawable.snow_background
        currentConditionText.contains("Fog", ignoreCase = true) || currentConditionText.contains("Mist", ignoreCase = true) -> R.drawable.fog_background
        else -> R.drawable.sunny_background
    }

    Box(Modifier.fillMaxSize()) {
        // Set the background image according to current weather condition
        Image(
            painter = painterResource(id = backgroundImage),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Create a list of ForecastItem from the forecast days in weather data
        val forecastItems = weather.forecast.forecastday.map { day ->
            ForecastItem(
                date = day.date,
                high = "${day.day.maxTemperatureCelsius}°",
                low = "${day.day.minTemperatureCelsius}°",
                wind = "${day.day.maxWindSpeedKph} km/h",
                humidity = "${day.day.averageHumidity}%",
                // Choose the icon based on the day's weather condition text
                icon = when {
                    day.day.condition.text.contains("Sunny", ignoreCase = true) -> R.drawable.sunny
                    day.day.condition.text.contains("Cloud", ignoreCase = true) -> R.drawable.cloudy
                    day.day.condition.text.contains("Rain", ignoreCase = true) -> R.drawable.rainy
                    day.day.condition.text.contains("Snow", ignoreCase = true) -> R.drawable.snow
                    day.day.condition.text.contains("Fog", ignoreCase = true) || day.day.condition.text.contains("Mist", ignoreCase = true) -> R.drawable.fog
                    day.day.condition.text.contains("Thunder", ignoreCase = true) -> R.drawable.thunderstorm
                    else -> R.drawable.sunny
                },
                condition = day.day.condition.text,
                precipitationType = "Precipitation",
                precipitationAmount = "${day.day.totalPrecipitationMillimeters} mm",
                precipitationProbability = "${day.day.dailyChanceOfRain}%",
                windDirection = "N/A" // Wind direction is not provided in forecast data
            )
        }

        // Show the forecast list as a scrollable column
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(forecastItems) { item ->
                DailyForecastItem(item)
            }
        }
    }
}

@Composable
fun DailyForecastItem(item: ForecastItem) {
    // Card representing one day of weather forecast
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Gray)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                // Weather condition icon
                Image(
                    painter = painterResource(id = item.icon),
                    contentDescription = "Forecast Icon",
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    // Date of forecast
                    Text(item.date, fontSize = 28.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    // Text description of the condition
                    Text(item.condition, fontSize = 26.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                // High temperature text
                Text("High: ${item.high}", fontSize = 24.sp, color = Color.White)
                Spacer(modifier = Modifier.width(16.dp))
                // Low temperature text
                Text("Low: ${item.low}", fontSize = 24.sp, color = Color.White)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Wind speed and direction text
                Text("Wind: ${item.wind} (${item.windDirection})", fontSize = 24.sp, color = Color.White)
                // Humidity percentage text
                Text("Humidity: ${item.humidity}", fontSize = 24.sp, color = Color.White)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Precipitation amount and type text
                Text("Precipitation: ${item.precipitationType} ${item.precipitationAmount}", fontSize = 24.sp, color = Color.White)
                // Chance of precipitation text
                Text("Chance: ${item.precipitationProbability}", fontSize = 24.sp, color = Color.White)
            }
        }
    }
}

// Data class holding daily forecast details to display
data class ForecastItem(
    val date: String,
    val high: String,
    val low: String,
    val wind: String,
    val humidity: String,
    val icon: Int,
    val condition: String,
    val precipitationType: String,
    val precipitationAmount: String,
    val precipitationProbability: String,
    val windDirection: String
)
