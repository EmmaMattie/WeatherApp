# WeatherApp 🌤️

An Android weather app built using **Kotlin**. It shows current and upcoming weather using **live data from WeatherAPI**.

## Features

- View current weather (live)
- View 7-day forecast
- Switch screens using bottom navigation
- Backgrounds and icons change based on live weather conditions
- Weather details include:
  - 📅 Date
  - 🌡️ Temperature (high & low)
  - 🌧️ Precipitation type 
  - 💧 Precipitation amount
  - 🌦️ Chance of precipitation
  - 💨 Wind speed and direction
  - 💧 Humidity
  - ☀️ Weather type 

## Screens

### Current Weather  
Shows today’s **live** weather:
- Weather icon and background update based on current conditions
- Precipitation amount and type
- Precipitation probability
- Current temperature (high & low)
- Wind direction and speed
- Humidity

### Daily Forecast  
Shows the **next 7 days' forecast**:
- Date and day of the week
- Temperature (high & low)
- Weather icon and background update based on daily condition
- Precipitation amount and type
- Precipitation probability
- Wind speed and direction
- Humidity

## Technology Used

- **Kotlin** – Main programming language
- **ViewModel** – Manages app data for each screen
- **Navigation Component** – Switch between Current Weather and Daily Forecast
- **WeatherAPI** – Provides live weather and 7-day forecast data
- **ImageView** – Displays dynamic weather icons and backgrounds
