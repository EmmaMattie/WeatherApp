package com.example.weatherapp // Ensure this package is correct

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.ui.theme.WeatherAppTheme // Ensure the theme is correctly imported

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    WeatherApp() // Call the WeatherApp Composable
                }
            }
        }
    }
}

@Composable
fun WeatherApp() {
    var showNow by remember { mutableStateOf(true) } // Track which screen (Now/Daily) to show

    Column(Modifier.fillMaxSize()) {
        // Navigation buttons to switch between "Now" and "Daily" screens
        Row(
            Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { showNow = true }) { Text("Now") }
            Button(onClick = { showNow = false }) { Text("Daily") }
        }

        // Display background image and current/daily screen based on showNow state
        Box(Modifier.fillMaxSize()) {
            Image(painter = painterResource(id = R.drawable.cloudy_background), contentDescription = null,
                modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
            if (showNow) NowScreen() else DailyScreen() // Conditionally show NowScreen or DailyScreen
        }
    }
}

@Composable
fun NowScreen() {
    // Display current weather information
    Column(
        Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
    ) {
        Image(painter = painterResource(id = R.drawable.cloudy), contentDescription = null, modifier = Modifier.size(100.dp))
        Text("14°C", fontSize = 72.sp, fontWeight = FontWeight.Bold, color = Color.White) // Current temperature
        Text("Cloudy", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = Color.White) // Current condition
        WeatherDetails("16°C", "12°C", "18 km/h", "65%") // Show weather details (high, low, wind, humidity)
    }
}

@Composable
fun WeatherDetails(high: String, low: String, wind: String, hum: String) {
    Spacer(Modifier.height(16.dp)) // Add space between elements
    Text("High: $high", fontSize = 28.sp, color = Color.White) // High temperature
    Text("Low:  $low", fontSize = 28.sp, color = Color.White)  // Low temperature
    Text("W: $wind", fontSize = 28.sp, color = Color.White)     // Wind speed
    Text("H:  $hum", fontSize = 28.sp, color = Color.White)     // Humidity percentage
}

@Composable
fun DailyScreen() {
    // Define forecast items for the next few days
    val items = listOf(
        ForecastItem("Fri", "3°", "0°", "10 km/h", "70%", R.drawable.rainy, "Rainy"),
        ForecastItem("Sat", "4°", "2°", "12 km/h", "65%", R.drawable.sun, "Sunny"),
        ForecastItem("Sun", "6°", "3°", "8 km/h", "60%", R.drawable.cloudy, "Cloudy"),
        ForecastItem("Mon", "8°", "5°", "15 km/h", "55%", R.drawable.rainy, "Rainy")
    )

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        // Split the items into two rows and display them
        items.chunked(2).forEach { rowItems ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                rowItems.forEach { ForecastCard(it) } // Display each forecast card
            }
            Spacer(Modifier.height(32.dp)) // Space between rows
        }
    }
}

@Composable
fun ForecastCard(item: ForecastItem) {
    // Display individual forecast card with day, weather, and details
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter = painterResource(id = item.icon), contentDescription = null, modifier = Modifier.size(60.dp)) // Weather icon
        Text(item.day, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White) // Day
        WeatherDetails(item.high, item.low, item.wind, item.humidity) // Weather details (high, low, wind, humidity)
        Text(item.condition, fontSize = 24.sp, color = Color.White) // Condition (e.g., Sunny, Rainy)
    }
}

data class ForecastItem(
    val day: String, // Day of the week
    val high: String, // High temperature
    val low: String, // Low temperature
    val wind: String, // Wind speed
    val humidity: String, // Humidity percentage
    val icon: Int, // Weather icon resource
    val condition: String // Weather condition (e.g., Sunny, Cloudy)
)
