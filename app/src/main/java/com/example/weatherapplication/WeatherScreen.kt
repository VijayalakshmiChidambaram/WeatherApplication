package com.example.weatherapplication

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import android.Manifest
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.material3.TextField
import androidx.compose.ui.graphics.Color

@Composable
fun WeatherScreen(viewModel: WeatherViewModel) {
    var cityInput by remember { mutableStateOf("") }
    val weatherState by viewModel.weatherState.collectAsState()

    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("weather_prefs", Context.MODE_PRIVATE)

    // Load last searched city
    LaunchedEffect(Unit) {
        val lastCity = sharedPreferences.getString("last_city", "")
        if (!lastCity.isNullOrEmpty()) {
            viewModel.getWeatherByCity(lastCity)
            cityInput = lastCity
        }
    }

    // Handle location permission
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.getWeatherByLocation()
        } else {
            Toast.makeText(context, "Location permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    // Request location permission on launch
    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.LightGray),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = cityInput,
            onValueChange = { cityInput = it },
            label = { Text("Enter a US City") },
            modifier = Modifier
                .background(Color.Black)
                .padding(5.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            if (cityInput.isNotEmpty()) {
                viewModel.getWeatherByCity(cityInput)

                // Save the last searched city
                with(sharedPreferences.edit()) {
                    putString("last_city", cityInput)
                    apply()
                }
            }
        }) {
            Text("Get Weather")
        }

        Spacer(modifier = Modifier.height(16.dp))

        weatherState.weatherInfo?.let { weatherInfo ->
            WeatherDisplay(weatherInfo)
        }
    }
}

@Composable
fun WeatherDisplay(weatherInfo: WeatherResponse) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "City: ${weatherInfo.name}")
        Text(text = "Temperature: ${weatherInfo.main.temp}°C")
        Text(text = "Description: ${weatherInfo.weather[0].description}")

        val iconCode = weatherInfo.weather[0].icon
        println("iconCode : $iconCode")
        val iconUrl = "https://openweathermap.org/img/wn/$iconCode@2x.png"
        val painter = rememberImagePainter(iconUrl)

        Image(
            painter = painter,
            contentDescription = weatherInfo.weather[0].description,
            modifier = Modifier.size(64.dp)
        )
    }
}