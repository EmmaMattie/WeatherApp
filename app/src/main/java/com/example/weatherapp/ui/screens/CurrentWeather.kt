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

@Composable
fun CurrentWeatherScreen() {
    // Fill the screen with a background and content
    Box(Modifier.fillMaxSize()) {

        // Background image
        Image(
            painter = painterResource(id = R.drawable.sunny_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Main column to hold all weather info
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {

            // Top row with weather icon and city/temp
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                // Sunny icon
                Image(
                    painter = painterResource(id = R.drawable.sunny),
                    contentDescription = "Weather Icon",
                    modifier = Modifier.size(100.dp)
                )
                Spacer(Modifier.width(16.dp))
                Column {
                    // City name and temperature
                    Text("Halifax", fontSize = 30.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Text("20°C", fontSize = 48.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(Modifier.height(4.dp))

            // Condition and high/low temps
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Column {
                    Text("Sunny", fontSize = 30.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(4.dp))
                    Text("H: 25°C   L: 16°C", fontSize = 30.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            // Adds empty space between UI elements
            Spacer(Modifier.height(16.dp))

            // Gray box with weather details
            Surface(
                modifier = Modifier.padding(4.dp),
                color = Color.Gray,
                shape = MaterialTheme.shapes.medium
            ) {
                Column(modifier = Modifier.padding(12.dp)) {

                    // Row 1: Wind Direction and Speed
                    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                        Column {
                            Text("Wind Direction", fontSize = 26.sp, color = Color.White)
                            Text("North", fontSize = 23.sp, color = Color.White)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Wind Speed", fontSize = 26.sp, color = Color.White)
                            Text("3 km/h", fontSize = 23.sp, color = Color.White)
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // Row 2: Humidity and Precipitation
                    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                        Column {
                            Text("Humidity", fontSize = 26.sp, color = Color.White)
                            Text("40%", fontSize = 23.sp, color = Color.White)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Precipitation", fontSize = 26.sp, color = Color.White)
                            Text("None", fontSize = 23.sp, color = Color.White)
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // Row 3: Chance of Precipitation
                    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                        Column {
                            Text("Chance of Precipitation", fontSize = 26.sp, color = Color.White)
                            Text("0%", fontSize = 22.sp, color = Color.White)
                        }
                        Column { } // Empty for spacing
                    }
                }
            }
        }
    }
}
