package com.bbj.myapplication.domain

import android.content.SharedPreferences
import com.bbj.myapplication.data.WeatherModel
import java.util.*

interface ForecastRepository {

    suspend fun getTodayWeatherForecast(cityName : String): WeatherModel

    suspend fun delete(date: Date)

    suspend fun update(date: Date, cityName : String,weatherModel: WeatherModel)

}
