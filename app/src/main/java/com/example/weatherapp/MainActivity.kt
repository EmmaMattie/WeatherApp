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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.jakewharton.threetenabp.AndroidThreeTen

class MainActivity : ComponentActivity() {

    // ViewModel holds weather data and app logic (handles how data is fetched, processed, and updated)
    private val viewModel: MainViewModel by viewModels()

    // Location client to get device location
    // I used 'lateinit' because fusedLocationClient can't be created immediately. It needs the app to start first, then I set it up inside onCreate().
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // Called when the app screen is created; sets up the initial state and UI
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ThreeTenABP for date/time formatting (needed for date in forecasts)
        // ThreeTenABP is a library that helps with modern date and time handling in Android apps, like formatting forecast dates.
        AndroidThreeTen.init(this)

        // Get location provider client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Ask user for location permission and then get location & weather
        requestLocationPermissionAndFetch()

        // Set the UI content with Jetpack Compose
        setContent {
            WeatherAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    DisplayUI(viewModel)  // Call composable to show UI
                }
            }
        }
    }

    // Request location permission if not granted, then fetch location
    private fun requestLocationPermissionAndFetch() {
        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) fetchLocation()
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission not granted, request it
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            // Permission already granted, fetch location now
            fetchLocation()
        }
    }

    // Fetch device location using fusedLocationClient
    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    // Get latitude, longitude as "lat,lng" string
                    val latLng = "${it.latitude},${it.longitude}"
                    // Fetch weather for this location from API
                    viewModel.fetchWeatherByLocation("Halifax")  // Use fixed city name for consistent forecast data
                    // Update location text shown in UI
                    viewModel.updateLocation("Halifax, Nova Scotia")
                }
            }
        }
    }
}

// Composable function to display main UI with navigation and screens
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayUI(viewModel: MainViewModel) {
    val navController = rememberNavController()

    // Collect current location string from ViewModel
    val location by viewModel.location.collectAsState(initial = "Loading...")

    // Collect current weather data from ViewModel
    val weather by viewModel.weather.collectAsState(initial = null)

    Scaffold(
        // Top app bar shows location and app name
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 140.dp)
                    ) {
                        Text(
                            text = location,
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

        // Bottom navigation bar to switch between Now and Daily forecast screens
        bottomBar = {
            NavigationBar(containerColor = Color.Gray) {
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Now") },
                    label = { Text("Now", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                    selected = navController.currentBackStackEntryAsState().value?.destination?.route == "now",
                    onClick = { navController.navigate("now") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Cloud, contentDescription = "Daily") },
                    label = { Text("Daily", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                    selected = navController.currentBackStackEntryAsState().value?.destination?.route == "daily",
                    onClick = { navController.navigate("daily") }
                )
            }
        }
    ) { innerPadding ->
        // Navigation host controls which screen to show
        NavHost(
            navController = navController,
            startDestination = "now",  // Start on current weather screen
            modifier = Modifier.padding(innerPadding)
        ) {
            // Show CurrentWeatherScreen composable when on "now"
            composable("now") { CurrentWeatherScreen(weather) }
            // Show DailyForecastScreen composable when on "daily"
            composable("daily") { DailyForecastScreen(weather) }
        }
    }
}
