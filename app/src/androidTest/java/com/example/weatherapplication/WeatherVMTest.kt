package com.example.weatherapplication

import android.location.Location
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.junit.MockitoRule

@ExperimentalCoroutinesApi
class WeatherViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule() // For LiveData testing

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var weatherRepository: WeatherRepositoryInterface

    @Mock
    private lateinit var locationProvider: LocationProvider

    private lateinit var viewModel: WeatherViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = WeatherViewModel(weatherRepository, locationProvider)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Clean up main dispatcher after each test
    }

    @Test
    fun getWeatherByCity_updatesweatherState_withWeatherInfoOnsuccess() = runTest {
        val weatherResponse = WeatherResponse(
            main = Main(temp = 25.0, humidity = 60),
            weather = listOf(Weather(description = "Clear sky", icon = "01d")),
            name = "New York"
        )

        `when`(weatherRepository.getWeatherByCity("New York")).thenReturn(weatherResponse)

        viewModel.getWeatherByCity("New York")

        val state = viewModel.weatherState.first()
        assertEquals(weatherResponse, state.weatherInfo)
        assertEquals(false, state.isLoading)
        assertEquals(null, state.errorMessage)
    }

    @Test
    fun getWeatherByCity_UpdatesweatherState_WithErrormessageonfailure() = runTest {
        `when`(weatherRepository.getWeatherByCity("InvalidCity")).thenThrow(RuntimeException("City not found"))

        viewModel.getWeatherByCity("InvalidCity")

        val state = viewModel.weatherState.first()
        assertEquals("Failed to fetch weather for city: City not found", state.errorMessage)
        assertEquals(false, state.isLoading)
        assertEquals(null, state.weatherInfo)
    }

    @Test
    fun getWeatherByLocation_UpdatesweatherState_withweatherInfoonsuccess() = runTest {
        val location = Location("").apply {
            latitude = 40.7128
            longitude = -74.0060
        }

        val weatherResponse = WeatherResponse(
            main = Main(temp = 25.0, humidity = 60),
            weather = listOf(Weather(description = "Clear sky", icon = "01d")),
            name = "New York"
        )

        `when`(locationProvider.getCurrentLocation()).thenReturn(location)
        `when`(weatherRepository.getWeatherByCoordinates(location.latitude, location.longitude)).thenReturn(weatherResponse)

        viewModel.getWeatherByLocation()

        val state = viewModel.weatherState.first()
        assertEquals(weatherResponse, state.weatherInfo)
        assertEquals(false, state.isLoading)
        assertEquals(null, state.errorMessage)
    }

    @Test
    fun getWeatherByLocation_updatesweatherState_witherrormessageonNullLocation() = runTest {
        `when`(locationProvider.getCurrentLocation()).thenReturn(null)

        viewModel.getWeatherByLocation()

        val state = viewModel.weatherState.first()
        assertEquals("Location is unavailable", state.errorMessage)
        assertEquals(false, state.isLoading)
        assertEquals(null, state.weatherInfo)
    }
}