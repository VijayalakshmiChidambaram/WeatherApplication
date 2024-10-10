package com.example.weatherapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val weatherRepository: WeatherRepository,
    private val getLocation: LocationFind
) : ViewModel() {

    private val _weatherState = MutableStateFlow(WeatherState())
    val weatherState: StateFlow<WeatherState> = _weatherState

    // Fetch weather by city name
    fun getWeatherByCity(city: String) {
        viewModelScope.launch {
            _weatherState.value = _weatherState.value.copy(isLoading = true)
            try {
                val response = weatherRepository.getWeatherByCity(city)
                _weatherState.value = _weatherState.value.copy(
                    weatherInfo = response,
                    isLoading = false,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _weatherState.value = _weatherState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to fetch weather for city: ${e.message}"
                )
            }
        }
    }

    // Fetch weather by geographic location
    fun getWeatherByLocation() {
        viewModelScope.launch {
            _weatherState.value = _weatherState.value.copy(isLoading = true)
            val location = getLocation.getCurrentLocation()
            location?.let {
                try {
                    val response = weatherRepository.getWeatherByCoordinates(it.latitude, it.longitude)
                    _weatherState.value = _weatherState.value.copy(
                        weatherInfo = response,
                        isLoading = false,
                        errorMessage = null
                    )
                } catch (e: Exception) {
                    _weatherState.value = _weatherState.value.copy(
                        isLoading = false,
                        errorMessage = "Failed to fetch weather for location: ${e.message}"
                    )
                }
            } ?: run {
                _weatherState.value = _weatherState.value.copy(
                    isLoading = false,
                    errorMessage = "Location is unavailable"
                )
            }
        }
    }
}

data class WeatherState(
    val weatherInfo: WeatherResponse? = null,
    val isLoading: Boolean = false, // For showing a loading spinner
    val errorMessage: String? = null // For displaying error messages
)
