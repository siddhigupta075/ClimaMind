package com.example.finalapp.data.repository

import com.example.finalapp.data.remote.api.RetrofitClient
import com.example.finalapp.data.remote.model.ForecastResponse
import com.example.finalapp.data.remote.model.WeatherResponse

class WeatherRepository {

    suspend fun getForecast(city: String): ForecastResponse {
        return RetrofitClient.api.getHourlyForecast(
            city,
            "d5c11d60f2e6f8c8d99f100c20510682"
        )
    }
    suspend fun getWeather(city: String): WeatherResponse {
        return RetrofitClient.api.getWeather(
            city,
            "d5c11d60f2e6f8c8d99f100c20510682"
        )
    }

}