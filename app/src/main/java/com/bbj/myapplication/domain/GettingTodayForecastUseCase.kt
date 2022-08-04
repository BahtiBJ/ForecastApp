package com.bbj.myapplication.domain

import com.bbj.myapplication.data.WeatherModel
import javax.inject.Inject

class GettingTodayForecastUseCase @Inject constructor(private val repository: ForecastRepository) {

    private var hasAPIKey: Boolean = false

    suspend fun execute(cityName: String): WeatherModel {
        if (!hasAPIKey)
            throw Exception("API ключ не установлен")
        return repository.getTodayWeatherForecast(cityName)
    }

    fun setAPIKey(apiKey: String) {
        if (apiKey != "" && apiKey.length > 28) {
            hasAPIKey = true
            repository.setAPIKey(apiKey)
        }
    }

}