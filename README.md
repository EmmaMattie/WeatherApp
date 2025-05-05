# WeatherApp 🌤️

An Android weather app built using **Kotlin**. It shows current and upcoming weather information using sample data.

## Features

- View current weather
- View 6-day forecast
- Switch screens using bottom navigation
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

- **Now Screen**  
  Shows today’s weather:
  - Precipitation amount and type
  - Precipitation probability
  - Weather icon 
  - Current temperature (high & low)
  - Wind direction and speed
  - Humidity

- **Daily Screen**  
  Shows next 6 days' forecast with:
  - Date and day of the week
  - Temperature (high & low)
  - Weather icon
  - Precipitation amount and type
  - Precipitation probability
  - Wind speed and direction
  - Humidity

## Technology Used

- **Kotlin** – Main programming language
- **ViewModel** – Manages app data for each screen
- **Navigation Component** – Switch between Now and Daily screens
- **Mock Weather Data** – Fake data used instead of API
- **ImageView** – Weather icons like sun, rain, cloud, fog, snow, thunderstorm
