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
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.TextStyle
import java.util.Locale

@Composable
fun DailyForecastScreen(weather: Weather?) {
    // Show loading text if weather data is not yet available
    if (weather == null) {
        Text("Loading...", modifier = Modifier.fillMaxSize(), color = Color.White)
        return
    }

    // Get current weather condition text for background choice
    val currentConditionText = weather.current.condition.text
    // Pick background image based on current weather
    val backgroundImage = when {
        currentConditionText.contains("Sunny", ignoreCase = true) -> R.drawable.sunny_background
        currentConditionText.contains("Cloud", ignoreCase = true) -> R.drawable.cloudy_background
        currentConditionText.contains("Rain", ignoreCase = true) -> R.drawable.rainy_background
        currentConditionText.contains("Snow", ignoreCase = true) -> R.drawable.snow_background
        currentConditionText.contains("Fog", ignoreCase = true) || currentConditionText.contains("Mist", ignoreCase = true) -> R.drawable.fog_background
        else -> R.drawable.sunny_background // default background if none match
    }

    Box(Modifier.fillMaxSize()) {
        // Background image covers entire screen
        Image(
            painter = painterResource(id = backgroundImage),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // crop to fill the box
        )

        // Function to format date string to readable format like "Tue, May 20"
        fun formatDate(dateString: String): String {
            val date = LocalDate.parse(dateString) // convert string to date object
            val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()) // get short day name
            val formatter = DateTimeFormatter.ofPattern("MMM d", Locale.getDefault()) // month and day format
            return "$dayOfWeek, ${date.format(formatter)}"
        }

        // Map each forecast day from API data into ForecastItem for UI display
        val forecastItems = weather.forecast.forecastday.map { day ->
            ForecastItem(
                date = formatDate(day.date), // formatted date string
                high = "${day.day.maxTemperatureCelsius}°", // max temp with degree symbol
                low = "${day.day.minTemperatureCelsius}°",  // min temp with degree symbol
                wind = "${day.day.maxWindSpeedKph} km/h",   // max wind speed with units
                humidity = "${day.day.averageHumidity}%",   // average humidity percentage
                // Chooses icon based on day's weather condition text
                icon = when {
                    day.day.condition.text.contains("Sunny", ignoreCase = true) -> R.drawable.sunny
                    day.day.condition.text.contains("Cloud", ignoreCase = true) -> R.drawable.cloudy
                    day.day.condition.text.contains("Rain", ignoreCase = true) -> R.drawable.rainy
                    day.day.condition.text.contains("Snow", ignoreCase = true) -> R.drawable.snow
                    day.day.condition.text.contains("Fog", ignoreCase = true) || day.day.condition.text.contains("Mist", ignoreCase = true) -> R.drawable.fog
                    day.day.condition.text.contains("Thunder", ignoreCase = true) -> R.drawable.thunderstorm
                    else -> R.drawable.sunny // default icon
                },
                condition = day.day.condition.text,                  // condition description text
                precipitationType = "Precipitation",                   // fixed label
                precipitationAmount = "${day.day.totalPrecipitationMillimeters} mm", // precipitation amount with units
                precipitationProbability = "${day.day.dailyChanceOfRain}%",         // chance of rain percentage
                windDirection = "N/A" // No wind direction information in forecast data
            )
        }

        // Display the forecast items in a vertical scrollable list
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp) // padding around list
        ) {
            // For each forecast day, show the weather info inside a styled card UI component
            items(forecastItems) { item ->
                DailyForecastItem(item) // display each day as a card
            }
        }
    }
}

@Composable
fun DailyForecastItem(item: ForecastItem) {
    // Card showing weather info for one day
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp), // space below card
        colors = CardDefaults.cardColors(containerColor = Color.Gray) // gray background color
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                // Weather icon image for the day
                Image(
                    painter = painterResource(id = item.icon),
                    contentDescription = "Forecast Icon",
                    modifier = Modifier.size(64.dp) // icon size 64dp
                )
                Spacer(modifier = Modifier.width(16.dp)) // space between icon and text

                Column {
                    // Date text
                    Text(item.date, fontSize = 28.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    // Condition description text
                    Text(item.condition, fontSize = 26.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(8.dp)) // vertical space

            Row(verticalAlignment = Alignment.CenterVertically) {
                // High temperature text
                Text("High: ${item.high}", fontSize = 24.sp, color = Color.White)
                Spacer(modifier = Modifier.width(16.dp)) // space between high and low
                // Low temperature text
                Text("Low: ${item.low}", fontSize = 24.sp, color = Color.White)
            }
            Spacer(modifier = Modifier.height(8.dp)) // vertical space

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Wind info text
                Text("Wind: ${item.wind} (${item.windDirection})", fontSize = 24.sp, color = Color.White)
                // Humidity text
                Text("Humidity: ${item.humidity}", fontSize = 24.sp, color = Color.White)
            }
            Spacer(modifier = Modifier.height(8.dp)) // vertical space

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Precipitation amount and label text
                Text("Precipitation: ${item.precipitationType} ${item.precipitationAmount}", fontSize = 24.sp, color = Color.White)
                // Chance of rain text
                Text("Chance: ${item.precipitationProbability}", fontSize = 24.sp, color = Color.White)
            }
        }
    }
}

// Data class holding daily forecast details for display
data class ForecastItem(
    val date: String,                 // formatted date string
    val high: String,
    val low: String,
    val wind: String,
    val humidity: String,
    val icon: Int,
    val condition: String,
    val precipitationType: String,
    val precipitationAmount: String,
    val precipitationProbability: String,
    val windDirection: String        // wind direction text or "N/A"
)
