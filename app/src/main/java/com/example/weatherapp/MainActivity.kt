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

class MainActivity : ComponentActivity() {

    // ViewModel to manage weather data
    private val viewModel: MainViewModel by viewModels()

    // Location provider client to get device location
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Request location permission and fetch location if granted
        requestLocationPermissionAndFetch()

        // Set Compose UI content
        setContent {
            WeatherAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    DisplayUI(viewModel)
                }
            }
        }
    }

    // Request location permission, or fetch location if already granted
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
            // Ask for permission if not granted
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            // Permission granted, fetch location
            fetchLocation()
        }
    }

    // Fetch last known location and update weather and location string
    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val latLng = "${it.latitude},${it.longitude}"
                    viewModel.fetchWeatherByLocation(latLng) // Fetch weather for location
                    viewModel.updateLocation("Halifax, Nova Scotia") // Show static name on UI
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayUI(viewModel: MainViewModel) {
    // Navigation controller for switching screens
    val navController = rememberNavController()

    // Collect location and weather data as Compose state
    val location by viewModel.location.collectAsState(initial = "Loading...")
    val weather by viewModel.weather.collectAsState(initial = null)

    Scaffold(
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
        NavHost(
            navController = navController,
            startDestination = "now",
            modifier = Modifier.padding(innerPadding)
        ) {
            // Show current weather screen with live data
            composable("now") { CurrentWeatherScreen(weather) }
            // Show daily forecast screen with live data
            composable("daily") { DailyForecastScreen(weather) }
        }
    }
}
