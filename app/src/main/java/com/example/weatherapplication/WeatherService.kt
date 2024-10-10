package com.example.weatherapplication

import com.squareup.moshi.JsonClass
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    // Get weather by city name
    @GET("weather")
    suspend fun getWeatherByCity(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric" // For temperature in Celsius
    ): WeatherResponse

    // Get weather by geographic coordinates (latitude and longitude)
    @GET("weather")
    suspend fun getWeatherByCoordinates(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric" // For temperature in Celsius
    ): WeatherResponse
}

@JsonClass(generateAdapter = true)
data class WeatherResponse(
    val main: Main,
    val weather: List<Weather>,
    val name: String // City name
)

@JsonClass(generateAdapter = true)
data class Main(
    val temp: Double,      // Temperature
    val humidity: Int      // Humidity
)

@JsonClass(generateAdapter = true)
data class Weather(
    val description: String, // Weather condition
    val icon: String         // Weather icon code (e.g., "01d")
)
