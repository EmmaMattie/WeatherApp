package com.example.weatherapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
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
            Text("Loading...", fontSize = 24.sp, color = Color.White)
        }
        return
    }

    val conditionText = weather.current.condition.text

    // Pick background image based on weather condition text
    val backgroundImage = when {
        conditionText.contains("Sunny", ignoreCase = true) -> R.drawable.sunny_background
        conditionText.contains("Cloud", ignoreCase = true) || conditionText.contains("Overcast", ignoreCase = true) -> R.drawable.cloudy_background
        conditionText.contains("Rain", ignoreCase = true) -> R.drawable.rainy_background
        conditionText.contains("Snow", ignoreCase = true) -> R.drawable.snow_background
        // For fog or mist conditions, show fog background
        conditionText.contains("Fog", ignoreCase = true) || conditionText.contains(
            "Mist",
            ignoreCase = true
        ) -> R.drawable.fog_background

        else -> R.drawable.sunny_background // Default background
    }

    // Pick icon image based on weather condition text
    val iconId = when {
        conditionText.contains("Sunny", ignoreCase = true) -> R.drawable.sunny
        conditionText.contains("Cloud", ignoreCase = true) || conditionText.contains("Overcast", ignoreCase = true) -> R.drawable.cloudy
        conditionText.contains("Rain", ignoreCase = true) -> R.drawable.rainy
        conditionText.contains("Snow", ignoreCase = true) -> R.drawable.snow
        conditionText.contains("Fog", ignoreCase = true) || conditionText.contains(
            "Mist",
            ignoreCase = true
        ) -> R.drawable.fog

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

        // Main content column that scrolls vertically with padding
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // Enable scrolling if content is large
                .padding(16.dp) // Add padding around content
        ) {
            // Row showing weather icon and temperature info side by side
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                // Weather icon image
                Image(
                    painter = painterResource(id = iconId),
                    contentDescription = "Weather Icon",
                    modifier = Modifier.size(100.dp)
                )
                Spacer(Modifier.width(16.dp)) // Space between icon and text

                // Column showing location and temperature
                Column {
                    Text(
                        "Halifax", // Fixed location name
                        fontSize = 30.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp)) // Space between location and temperature
                    Text(
                        "${weather.current.temperatureCelsius}°C", // Current temperature value
                        fontSize = 48.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(4.dp)) // Small vertical space

            // Row showing condition description and high/low temperature for the day
            Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Column {
                    // Text showing current weather condition
                    Text(
                        conditionText,
                        fontSize = 30.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(4.dp)) // Space below condition text

                    // Text showing the high and low temperatures for the day
                    Text(
                        "H: ${weather.forecast.forecastday[0].day.maxTemperatureCelsius}°  L: ${weather.forecast.forecastday[0].day.minTemperatureCelsius}°",
                        fontSize = 30.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(16.dp)) // Larger space before additional details

            // Gray background surface showing extra weather details
            Surface(
                modifier = Modifier.padding(4.dp),
                color = Color.Gray,
                shape = MaterialTheme.shapes.medium // Rounded corners shape
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    // Row for wind direction and speed info
                    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                        Column {
                            Text("Wind Direction", fontSize = 26.sp, color = Color.White)
                            Text(
                                weather.current.windDirection,
                                fontSize = 23.sp,
                                color = Color.White
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Wind Speed", fontSize = 26.sp, color = Color.White)
                            Text(
                                "${weather.current.windSpeedKph} km/h",
                                fontSize = 23.sp,
                                color = Color.White
                            )
                        }
                    }
                    Spacer(Modifier.height(16.dp)) // Space between rows

                    // Row for humidity and precipitation amount
                    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                        Column {
                            Text("Humidity", fontSize = 26.sp, color = Color.White)
                            Text(
                                "${weather.current.humidity}%",
                                fontSize = 23.sp,
                                color = Color.White
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Precipitation", fontSize = 26.sp, color = Color.White)
                            Text(
                                "${weather.current.precipitationMillimeters} mm",
                                fontSize = 23.sp,
                                color = Color.White
                            )
                        }
                    }
                    Spacer(Modifier.height(16.dp)) // Space between rows

                    // Row for chance of precipitation
                    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                        Column {
                            Text("Chance of Precipitation", fontSize = 26.sp, color = Color.White)
                            Text(
                                "${weather.forecast.forecastday[0].day.dailyChanceOfRain}%",
                                fontSize = 22.sp,
                                color = Color.White
                            )
                        }
                        Column { // Empty column for spacing }
                        }
                    }
                }
            }
        }
    }
}
