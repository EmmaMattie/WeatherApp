package com.example.weatherapp

import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.navigation.compose.*
import com.example.weatherapp.ui.theme.WeatherAppTheme

// MainActivity is the entry point of the app and sets the UI content.
class MainActivity : ComponentActivity() {

    // Creates an instance of MainViewModel using the built-in viewModels() delegate.
    // ViewModel holds and manages the app's weather data.
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Sets the content view using Jetpack Compose.
        setContent {

            // Applies the app's theme.
            WeatherAppTheme {

                // Creates a full-screen surface to hold the UI.
                Surface(modifier = Modifier.fillMaxSize()) {

                    // Calls the main UI function and passes the ViewModel.
                    // DisplayUI handles navigation and screen display.
                    DisplayUI(viewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayUI(viewModel: MainViewModel) {

    // NavController handles switching between "now" and "daily" screens.
    val navController = rememberNavController()

    // Scaffold provides the basic layout structure: top bar, bottom nav, and content area.
    Scaffold(
        topBar = {
            // Top bar displays the location name centered and app name on the left.
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 140.dp)
                    ) {
                        Text(
                            text = "Halifax, Nova Scotia", // Static location title
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.CenterStart)
                        )
                    }
                },
                navigationIcon = {
                    // App name appears on the left like a logo.
                    Text(
                        text = "WeatherApp",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                },
                // Top bar background color
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Gray)
            )
        },
        bottomBar = {
            // Bottom navigation bar to switch between Now and Daily screens.
            NavigationBar(containerColor = Color.Gray) {
                // Now tab
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Now") }, // Home icon for Now screen
                    label = { Text("Now", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                    selected = navController.currentBackStackEntryAsState().value?.destination?.route == "now",
                    onClick = { navController.navigate("now") }
                )
                // Daily tab
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Cloud, contentDescription = "Daily") }, // Cloud icon for Daily screen
                    label = { Text("Daily", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                    selected = navController.currentBackStackEntryAsState().value?.destination?.route == "daily",
                    onClick = { navController.navigate("daily") }
                )
            }
        }
    ) { innerPadding ->
        // NavHost manages which screen is visible based on the selected route.
        NavHost(
            navController = navController,
            startDestination = "now", // Default screen when app opens
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("now") { NowScreen() }     // Shows current weather
            composable("daily") { DailyScreen() } // Shows forecast for the week
        }
    }
}

@Composable
fun NowScreen() {
    // Fill the screen with a background and content
    Box(Modifier.fillMaxSize()) {

        // Background image for sunny weather
        Image(
            painter = painterResource(id = R.drawable.sunny_background),
            contentDescription = null, // No need for screen readers
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Scale image to fill screen
        )

        // Scrollable column for weather content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // Enables scrolling
                .padding(16.dp)
        ) {
            // Top section with weather icon and temperature info
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.Start // Align items to the left
            ) {
                // Sunny icon (100x100 dp)
                Image(
                    painter = painterResource(id = R.drawable.sunny),
                    contentDescription = "Weather Icon",
                    modifier = Modifier.size(100.dp)
                )
                Spacer(Modifier.width(16.dp)) // Space between icon and text

                // City name and temperature
                Column {
                    Text("Halifax", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(Modifier.height(8.dp))
                    Text("20°C", fontSize = 48.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }

            Spacer(Modifier.height(4.dp))

            // Weather condition and high/low temperature below the top row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Column {
                    Text("Sunny", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(Modifier.height(4.dp))
                    Text("H: 25°C   L: 16°C", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }

            Spacer(Modifier.height(16.dp))

            // Surface container for weather details
            Surface(
                modifier = Modifier.padding(4.dp),
                color = Color.Gray,
                shape = MaterialTheme.shapes.medium // Rounded edges
            ) {
                Column(modifier = Modifier.padding(12.dp)) {

                    // Row 1: Wind Direction and Wind Speed
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Wind Direction", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("North", fontSize = 23.sp, color = Color.White)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Wind Speed", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("3 km/h", fontSize = 23.sp, color = Color.White)
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // Row 2: Humidity and Precipitation
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Humidity", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("40%", fontSize = 23.sp, color = Color.White)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Precipitation", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("None", fontSize = 23.sp, color = Color.White)
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // Row 3: Chance of Rain
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Chance of Precipitation", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("0%", fontSize = 22.sp, color = Color.White)
                        }

                        // Right side left blank for spacing
                        Column {}
                    }
                }
            }
        }
    }
}

@Composable
fun DailyScreen() {
    // List of forecast items with weather data
    val items = listOf(
        ForecastItem("Fri May 2", "3°", "0°", "29 km/h", "80%", R.drawable.rainy, "Rainy", "Rain,", "11 mm", "90%", "North"),
        ForecastItem("Sat May 3", "19°", "11°", "4 km/h", "30%", R.drawable.sunny, "Sunny", "None", "", "0%", "East"),
        ForecastItem("Sun May 4", "7°", "3°", "17 km/h", "70%", R.drawable.cloudy, "Cloudy", "None", "", "40%", "West"),
        ForecastItem("Mon May 5", "6°", "1°", "35 km/h", "75%", R.drawable.thunderstorm, "Thunderstorm", "Thunderstorm,", "8 mm", "100%", "South"),
        ForecastItem("Tue May 6", "-2°", "-6°", "15 km/h", "85%", R.drawable.snow, "Snow", "Snow,", "12 cm", "95%", "Northwest"),
        ForecastItem("Wed May 7", "4°", "2°", "10 km/h", "90%", R.drawable.fog, "Fog", "Fog", "", "60%", "East")
    )

    // Box to hold background image and weather items
    Box(Modifier.fillMaxSize()) {
        // Background image for the screen
        Image(
            painter = painterResource(id = R.drawable.sunny_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // LazyColumn to display the list of forecast items
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // For each forecast item, display it using DailyForecastItem
            items(items) { item ->
                DailyForecastItem(item)
            }
        }
    }
}

@Composable
fun DailyForecastItem(item: ForecastItem) {
    // Card to display individual weather forecast details
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Gray)
    ) {
        // Column to arrange all weather information vertically
        Column(modifier = Modifier.padding(16.dp)) {
            // Row for displaying date, weather icon, and condition
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
                    Text(item.date, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(item.condition, fontSize = 26.sp, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Row for displaying high and low temperatures
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("High: ${item.high}", fontSize = 24.sp, color = Color.White)
                Spacer(modifier = Modifier.width(16.dp))
                Text("Low: ${item.low}", fontSize = 24.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Row for displaying wind and humidity details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Wind: ${item.wind} (${item.windDirection})", fontSize = 24.sp, color = Color.White)
                Text("Humidity: ${item.humidity}", fontSize = 24.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Row for displaying precipitation details and chance of precipitation
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

// Holds all the weather info for one day, used to display in the daily forecast screen
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
