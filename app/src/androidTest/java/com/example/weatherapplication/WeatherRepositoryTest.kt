package com.example.weatherapplication

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

@ExperimentalCoroutinesApi
class WeatherRepositoryTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var weatherService: WeatherService

    private lateinit var weatherRepository: WeatherRepositoryInterface

    @Before
    fun setUp() {
        // Create a mock instance of WeatherService
        weatherService = mock(WeatherService::class.java)
        // Initialize the repository with the mock service
        weatherRepository = WeatherRepository(weatherService)
    }

    @Test
    fun getWeatherByCoordinates_returnsweatherresponse_onSuccess() = runBlocking {
        val latitude = 40.7128
        val longitude = -74.0060
        val expectedResponse = WeatherResponse(
            main = Main(temp = 25.0, humidity = 50),
            weather = listOf(Weather(description = "clear sky", icon = "01d")),
            name = "New York"
        )

        // Mock the service's response, using matchers for all parameters
        `when`(weatherService.getWeatherByCoordinates(eq(latitude), eq(longitude), anyString(), anyString())).thenReturn(expectedResponse)

        // Call the method on the repository interface
        val response = weatherRepository.getWeatherByCoordinates(latitude, longitude)

        // Verify the result matches the expected response
        assertEquals(expectedResponse, response)
    }
}
