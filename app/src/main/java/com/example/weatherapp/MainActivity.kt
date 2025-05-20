package com.example.weatherapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.*
import com.example.weatherapp.ui.screens.CurrentWeatherScreen
import com.example.weatherapp.ui.screens.DailyForecastScreen
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

// MainActivity is the app's entry point and controls UI and location access
class MainActivity : ComponentActivity() {

    // Create ViewModel instance for data handling
    private val viewModel: MainViewModel by viewModels()

    // Location client to get device GPS coordinates
    // 'lateinit var' means the variable is created now but given a value later
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize fused location client for GPS data
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Request location permission from user and fetch location if granted
        requestLocationPermissionAndFetch()

        // Set Jetpack Compose content for the UI
        setContent {
            WeatherAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    // Show main UI screen and pass ViewModel for data
                    DisplayUI(viewModel)
                }
            }
        }
    }

    // Checks if location permission is granted, requests if not
    // Calls fetchLocation() after permission is granted
    private fun requestLocationPermissionAndFetch() {
        // Launcher for permission request result
        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) fetchLocation() // If granted, get location
        }

        // Check if permission is already granted
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Launch permission request dialog
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            // Permission already granted, fetch location directly
            fetchLocation()
        }
    }

    // Gets the last known location from the device
    private fun fetchLocation() {
        // Confirm permission still granted before accessing location
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    // Format latitude and longitude for API call
                    val latLng = "${it.latitude},${it.longitude}"
                    // Tell ViewModel to fetch weather for this location
                    viewModel.fetchWeatherByLocation(latLng)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayUI(viewModel: MainViewModel) {
    // NavController manages app screen navigation
    val navController = rememberNavController()

    Scaffold(
        // Top app bar with location name and app title
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 140.dp) // Position text centered
                    ) {
                        Text(
                            text = "Halifax, Nova Scotia", // Static for now
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.CenterStart)
                        )
                    }
                },
                navigationIcon = {
                    Text(
                        text = "WeatherApp",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Gray)
            )
        },
        // Bottom navigation bar to switch screens
        bottomBar = {
            NavigationBar(containerColor = Color.Gray) {
                // "Now" tab with home icon
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Now") },
                    label = { Text("Now", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                    selected = navController.currentBackStackEntryAsState().value?.destination?.route == "now",
                    onClick = { navController.navigate("now") }
                )
                // "Daily" tab with cloud icon
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Cloud, contentDescription = "Daily") },
                    label = { Text("Daily", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                    selected = navController.currentBackStackEntryAsState().value?.destination?.route == "daily",
                    onClick = { navController.navigate("daily") }
                )
            }
        }
    ) { innerPadding ->
        // NavHost shows current screen based on navigation state
        NavHost(
            navController = navController,
            startDestination = "now", // Show current weather first
            modifier = Modifier.padding(innerPadding)
        ) {
            // Composable for current weather screen
            composable("now") { CurrentWeatherScreen() }
            // Composable for daily forecast screen
            composable("daily") { DailyForecastScreen() }
        }
    }
}
