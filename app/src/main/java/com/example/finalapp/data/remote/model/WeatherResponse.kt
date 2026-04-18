package com.example.finalapp.data.remote.model

data class WeatherResponse(
    val main: Main,
    val weather: List<Weather> // 🔥 THIS WAS MISSING
)

