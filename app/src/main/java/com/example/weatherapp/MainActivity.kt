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

            // Surface container for weather details (wind, humidity, etc.)
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
                            Text("North", fontSize = 22.sp, color = Color.White)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Wind Speed", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("3 km/h", fontSize = 22.sp, color = Color.White)
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
                            Text("5%", fontSize = 22.sp, color = Color.White)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Precipitation", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("None", fontSize = 22.sp, color = Color.White)
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // Row 3: Chance of Rain
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Chance of Rain", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color.White)
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
    // List of forecast items for different days
    val items = listOf(
        ForecastItem("Fri May 2", "3°", "0°", "Wind: 29 km/h", "Humidity: 65%", R.drawable.rainy, "Rainy", "Rain", "11 mm", "90%", "North"),
        ForecastItem("Sat May 3", "19°", "11°", "Wind: 4 km/h", "Humidity: 72%", R.drawable.sunny, "Sunny", "None", "0 mm", "0%", "East"),
        ForecastItem("Sun May 4", "7°", "3°", "Wind: 17 km/h", "Humidity: 60%", R.drawable.cloudy, "Partly Cloudy", "Clouds", "5 mm", "40%", "West")
    )

    // Main container with background and scrollable content
    Box(Modifier.fillMaxSize()) {

        // Background image
        Image(
            painter = painterResource(id = R.drawable.sunny_background),
            contentDescription = null, // No screen reader needed
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Stretch image to fit screen
        )

        // Scrollable list of forecast items
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp) // Add padding around the list
        ) {
            // Add each forecast item to the list
            items(items) { item ->
                DailyForecastItem(item) // Show item using custom UI
            }
        }
    }
}

@Composable
fun DailyForecastItem(item: ForecastItem) {
    // Card to hold each forecast item
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp), // Space between items
        colors = CardDefaults.cardColors(containerColor = Color.Gray) // Background color
    ) {
        // Content inside the card
        Column(modifier = Modifier.padding(16.dp)) {

            // Top row: Icon, Date, and Weather Condition
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
                    Text(item.date, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(item.condition, fontSize = 20.sp, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Row for High and Low temperatures
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("High: ${item.high}", color = Color.White, fontSize = 22.sp)
                Spacer(modifier = Modifier.width(16.dp))
                Text("Low: ${item.low}", color = Color.White, fontSize = 22.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Row for Precipitation Type and Amount
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Precipitation: ${item.precipitationType}", color = Color.White, fontSize = 22.sp)
                Text("Amount: ${item.precipitationAmount}", color = Color.White, fontSize = 22.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Row for Humidity and Chance of Rain
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(item.humidity, color = Color.White, fontSize = 22.sp)
                Text("Chance of Rain: ${item.precipitationProbability}", color = Color.White, fontSize = 22.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Row for Wind and Wind Direction
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(item.wind, color = Color.White, fontSize = 22.sp)
                Text("Wind Direction: ${item.windDirection}", color = Color.White, fontSize = 22.sp)
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
