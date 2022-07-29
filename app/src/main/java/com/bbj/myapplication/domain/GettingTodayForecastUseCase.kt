package com.bbj.myapplication.domain

import com.bbj.myapplication.data.WeatherModel
import javax.inject.Inject

class GettingTodayForecastUseCase @Inject constructor(private val repository: ForecastRepository) {

    suspend fun execute(cityName : String) : WeatherModel{
        return repository.getTodayWeatherForecast(cityName)
    }

}