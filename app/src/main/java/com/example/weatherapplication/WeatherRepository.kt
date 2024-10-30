package com.example.weatherapplication

import kotlinx.coroutines.runBlocking

interface WeatherRepositoryInterface {
    suspend fun getWeatherByCity(city: String): WeatherResponse
    suspend fun getWeatherByCoordinates(latitude: Double, longitude: Double): WeatherResponse
}

/**
 * Repository functions to fetch weather by city name, by geographic coordinates using API key
 */

class WeatherRepository(private val weatherService: WeatherService) : WeatherRepositoryInterface{

    private val apiKey = "1c1dab9e7b13cb240b29f3917c79ddc0"

    override suspend fun getWeatherByCity(city: String): WeatherResponse {
        return weatherService.getWeatherByCity(city, apiKey)
    }

    override suspend fun getWeatherByCoordinates(latitude: Double, longitude: Double): WeatherResponse {
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

