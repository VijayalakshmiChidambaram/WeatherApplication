package com.example.weatherapplication

import kotlinx.coroutines.runBlocking

class WeatherRepository(private val weatherService: WeatherService) {

    private val apiKey = "1c1dab9e7b13cb240b29f3917c79ddc0" // Add your API key

    // Fetch weather by city name
    suspend fun getWeatherByCity(city: String): WeatherResponse {
        return weatherService.getWeatherByCity(city, apiKey)
    }

    // Fetch weather by geographic coordinates
    suspend fun getWeatherByCoordinates(latitude: Double, longitude: Double): WeatherResponse {
        return weatherService.getWeatherByCoordinates(latitude, longitude, apiKey)
    }
}

fun main() {
    // Create Retrofit instance
    val weatherService = createWeatherService()

    // Create instance of WeatherRepository with the WeatherService
    val weatherRepository = WeatherRepository(weatherService)

    // Use runBlocking to call suspend functions
    runBlocking {
        try {
            // Call to getWeatherByCity and print the result
            val cityWeather = weatherRepository.getWeatherByCity("New York")
            println("Weather in New York: ${cityWeather}")

            // Optionally test getWeatherByCoordinates
            val coordinatesWeather = weatherRepository.getWeatherByCoordinates(40.7128, -74.0060)
            println("Weather by coordinates (New York): ${coordinatesWeather}")
        } catch (e: Exception) {
            // Handle any exceptions that might occur during the API call
            println("Error: ${e.message}")
        }
    }
}

