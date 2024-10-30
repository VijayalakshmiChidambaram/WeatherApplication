package com.example.weatherapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent


class MainActivity : ComponentActivity() {

    private lateinit var viewModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /** ViewModel and dependencies initialization
         */
        val weatherService = createWeatherService()
        val weatherRepository = WeatherRepository(weatherService)
        val locationFinder = LocationFind(this)
        viewModel = WeatherViewModel(weatherRepository, locationFinder)

        setContent {
            WeatherScreen(viewModel)
        }
    }
}