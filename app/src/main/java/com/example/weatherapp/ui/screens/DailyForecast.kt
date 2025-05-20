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

@Composable
fun DailyForecastScreen() {
    // List of forecast items with weather data
    val items = listOf(
        ForecastItem("Fri, May 2", "3°", "0°", "29 km/h", "80%", R.drawable.rainy, "Rainy", "Rain,", "11 mm", "90%", "North"),
        ForecastItem("Sat, May 3", "19°", "11°", "4 km/h", "30%", R.drawable.sunny, "Sunny", "None", "", "0%", "East"),
        ForecastItem("Sun, May 4", "7°", "3°", "17 km/h", "70%", R.drawable.cloudy, "Cloudy", "None", "", "40%", "West"),
        ForecastItem("Mon, May 5", "6°", "1°", "35 km/h", "75%", R.drawable.thunderstorm, "Thunderstorm", "Thunderstorm,", "8 mm", "100%", "South"),
        ForecastItem("Tue, May 6", "-2°", "-6°", "15 km/h", "85%", R.drawable.snow, "Snow", "Snow,", "12 cm", "95%", "Northwest"),
        ForecastItem("Wed, May 7", "4°", "2°", "10 km/h", "90%", R.drawable.fog, "Fog", "Fog", "", "60%", "East")
    )

    // Background image + forecast list
    Box(Modifier.fillMaxSize()) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.sunny_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Scrollable list of forecast cards
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(items) { item ->
                DailyForecastItem(item)
            }
        }
    }
}

@Composable
fun DailyForecastItem(item: ForecastItem) {
    // Card to show one day of forecast
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Gray)
    ) {
        // All forecast info inside the card
        Column(modifier = Modifier.padding(16.dp)) {

            // Row with icon, date, and condition
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Image(
                    painter = painterResource(id = item.icon),
                    contentDescription = "Forecast Icon",
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(item.date, fontSize = 28.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    Text(item.condition, fontSize = 26.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            // Adds empty space between UI elements
            Spacer(modifier = Modifier.height(8.dp))

            // High and low temperature side by side
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("High: ${item.high}", fontSize = 24.sp, color = Color.White)
                Spacer(modifier = Modifier.width(16.dp))
                Text("Low: ${item.low}", fontSize = 24.sp, color = Color.White)
            }

            // Adds vertical space of 8dp between UI elements
            Spacer(modifier = Modifier.height(8.dp))

            // Wind and humidity row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Wind: ${item.wind} (${item.windDirection})", fontSize = 24.sp, color = Color.White)
                Text("Humidity: ${item.humidity}", fontSize = 24.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Precipitation info row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Precipitation: ${item.precipitationType} ${item.precipitationAmount}", fontSize = 24.sp, color = Color.White)
                Text("Chance: ${item.precipitationProbability}", fontSize = 24.sp, color = Color.White)
            }
        }
    }
}

// Holds one day's forecast details
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
