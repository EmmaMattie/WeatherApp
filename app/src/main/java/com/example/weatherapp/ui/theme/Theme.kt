package com.example.weatherapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Define weather-inspired color scheme with darker tones
private val LightColors = lightColorScheme(
    primary = Color(0xFF006BB6), // Darker Blue for clear skies
    secondary = Color(0xFF4A9A2B), // Dark Green for fresh weather
    background = Color(0xFF2B2B2B), // Dark background for cloudy weather
    surface = Color(0xFF333333), // Dark surface for contrast
    onPrimary = Color.White, // White text on primary color
    onSecondary = Color.Black, // Black text on secondary color
    onBackground = Color.White, // White text on background color
    onSurface = Color.White // White text on surface color
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF3388CC), // Darker Blue for clear skies
    secondary = Color(0xFF4B8C2F), // Dark Green for fresh weather
    background = Color(0xFF121212), // Dark background for cloudy weather
    surface = Color(0xFF1F1F1F), // Dark surface for contrast
    onPrimary = Color.Black, // Black text on primary color
    onSecondary = Color.White, // White text on secondary color
    onBackground = Color.White, // White text on background color
    onSurface = Color.White // White text on surface color
)

@Composable
fun WeatherAppTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}
