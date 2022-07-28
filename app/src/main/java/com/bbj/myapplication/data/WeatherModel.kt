package com.bbj.myapplication.data


data class WeatherModel(
    val temp: Double,
    val tempFeelsLike: Double,
    val description: String,
    val tempMin: Double,
    val tempMax: Double,
    val humidity: Int,
    val windSpeed: Double,
    val pressure: Int
)
