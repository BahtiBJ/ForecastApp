package com.bbj.myapplication.domain

import com.bbj.myapplication.data.WeatherModel

interface ForecastRepository {

    suspend fun getTodayWeatherForecast(cityName : String): WeatherModel

}
