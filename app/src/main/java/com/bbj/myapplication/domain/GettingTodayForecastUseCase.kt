package com.bbj.myapplication.domain

import android.content.SharedPreferences
import com.bbj.myapplication.data.WeatherModel

class GettingTodayForecastUseCase(private val repository: ForecastRepository) {

    suspend fun execute(cityName : String) : WeatherModel{
        return repository.getTodayWeatherForecast(cityName)
    }

}