package com.example.weatherapplication

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

// Helper function to create Retrofit instance with Moshi
fun createWeatherService(): WeatherService {
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory()) // For handling Kotlin-specific features
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/data/2.5/") // Base URL for OpenWeatherMap API
        .client(OkHttpClient())
        .addConverterFactory(MoshiConverterFactory.create(moshi)) // Use Moshi for JSON conversion
        .build()

    return retrofit.create(WeatherService::class.java)
}
