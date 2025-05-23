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
    if (weather == null) {
        Text("Loading...", modifier = Modifier.fillMaxSize(), color = Color.White)
        return
    }
    val currentConditionText = weather.current.condition.text

    val backgroundImage = when {
        currentConditionText.contains("Thunder", ignoreCase = true) || currentConditionText.contains("Storm", ignoreCase = true) -> R.drawable.thunderstorm_background
        currentConditionText.contains("Snow", ignoreCase = true) -> R.drawable.snow_background
        currentConditionText.contains("Rain", ignoreCase = true) -> R.drawable.rainy_background
        currentConditionText.contains("Fog", ignoreCase = true) || currentConditionText.contains("Mist", ignoreCase = true) -> R.drawable.fog_background
        currentConditionText.contains("Cloud", ignoreCase = true) || currentConditionText.contains("Overcast", ignoreCase = true) -> R.drawable.cloudy_background
        currentConditionText.contains("Sunny", ignoreCase = true) -> R.drawable.sunny_background
        else -> R.drawable.sunny_background
    }


    Box(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = backgroundImage),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        fun formatDate(dateString: String): String {
            val date = LocalDate.parse(dateString)
            val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
            val formatter = DateTimeFormatter.ofPattern("MMM d", Locale.getDefault())
            return "$dayOfWeek, ${date.format(formatter)}"
        }

        val forecastItems = weather.forecast.forecastday.map { day ->
            ForecastItem(
                date = formatDate(day.date),
                high = "${day.day.maxTemperatureCelsius}°",
                low = "${day.day.minTemperatureCelsius}°",
                wind = "${day.day.maxWindSpeedKph} km/h",
                humidity = "${day.day.averageHumidity}%",
                icon = when {
                    day.day.condition.text.contains("Sunny", ignoreCase = true) -> R.drawable.sunny
                    day.day.condition.text.contains("Cloud", ignoreCase = true) || day.day.condition.text.contains("Overcast", ignoreCase = true) -> R.drawable.cloudy
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
                windDirection = "N/A"
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            items(forecastItems) { item ->
                DailyForecastItem(item)
            }
        }
    }
}

@Composable
fun DailyForecastItem(item: ForecastItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xAA444444))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Image(
                    painter = painterResource(id = item.icon),
                    contentDescription = "Forecast Icon",
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(item.date, fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(item.condition, fontSize = 16.sp, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                Text("High: ${item.high}", fontSize = 16.sp, color = Color.White)
                Spacer(modifier = Modifier.width(16.dp))
                Text("Low: ${item.low}", fontSize = 16.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text("Wind: ${item.wind} (${item.windDirection})", fontSize = 16.sp, color = Color.White)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Humidity: ${item.humidity}", fontSize = 16.sp, color = Color.White)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Precipitation: ${item.precipitationType} ${item.precipitationAmount}", fontSize = 16.sp, color = Color.White)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Chance: ${item.precipitationProbability}", fontSize = 16.sp, color = Color.White)
        }
    }
}

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
