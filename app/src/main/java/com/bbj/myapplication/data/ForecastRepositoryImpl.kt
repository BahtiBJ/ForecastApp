package com.bbj.myapplication.data

import android.util.Log
import com.bbj.myapplication.data.database.DatabaseDAO
import com.bbj.myapplication.data.database.DatabaseModel
import com.bbj.myapplication.data.retrofit.RetrofitDAO
import com.bbj.myapplication.data.retrofit.WebForecastModel
import com.bbj.myapplication.domain.ForecastRepository
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

class ForecastRepositoryImpl
    (private val databaseDAO: DatabaseDAO, private val retrofitDAO: RetrofitDAO?) :
    ForecastRepository {

    private val dateFormatter = SimpleDateFormat("dd-MM-yy", Locale.US)
    private val today = dateFormatter.format(Date())

    private val defaultWeatherModel: WeatherModel = WeatherModel(
        404.0, 200.0, "Неизвестно",
        -404.0,
        1000.0,
        100,
        300.0,
        500
    )

    override suspend fun getTodayWeatherForecast(cityName: String): WeatherModel {
        val resultFromDB = databaseDAO.getByDate(today)
        val resultWeatherModel : WeatherModel
        Log.d("TAG", "CITYNAME === $cityName")
        if (resultFromDB.isEmpty()) {
            val weatherModel = getWeatherModelByRetrofit(cityName)
            Log.d("TAG", "Result empty Rep get from web = ${weatherModel}")
            if (weatherModel.temp != 404.0) {
                databaseDAO.insertWithRefresh(DatabaseModel(today,cityName,weatherModel,0))
            }
            resultWeatherModel = weatherModel
        } else {
            val currentForecast = containsForecastForCity(resultFromDB, cityName)
            if (currentForecast != null) {
                Log.d("TAG", "Rep get from db = ${currentForecast.weatherModel}")
                resultWeatherModel = currentForecast.weatherModel
            } else {
                val weatherModel = getWeatherModelByRetrofit(cityName)
                Log.d("TAG", "Result not empty Rep get from web = ${weatherModel}")
                if (weatherModel.temp != 404.0) {
                    databaseDAO.insert(DatabaseModel(today,cityName, weatherModel,0))
                }
                resultWeatherModel = weatherModel
            }
        }
        return resultWeatherModel
    }

    override suspend fun delete(date: Date) {
        val formattedDate = dateFormatter.format(date)
        databaseDAO.delete(formattedDate)
    }

    override suspend fun update(date: Date, cityName: String, weatherModel: WeatherModel) {
        val formattedDate = dateFormatter.format(date)
        databaseDAO.insert(DatabaseModel(formattedDate, cityName, weatherModel, 0))
    }

    private suspend fun getWeatherModelByRetrofit(cityName: String): WeatherModel {
        val response = retrofitDAO?.getForecastFromWeb(cityName)
        if (response!!.isSuccessful) {
            val resultFromWeb = response.body()
            delay(1500)
            val weatherModel = getWeatherModel(resultFromWeb!!)
            return weatherModel
        }
        return defaultWeatherModel
    }

    fun getWeatherModel(webForecast: WebForecastModel): WeatherModel {
        return WeatherModel(
            temp = webForecast.main.temp,
            tempFeelsLike = webForecast.main.feels_like,
            description = webForecast.weather[0].description,
            tempMin = webForecast.main.temp_min,
            tempMax = webForecast.main.temp_max,
            humidity = webForecast.main.humidity,
            windSpeed = webForecast.wind.speed,
            pressure = webForecast.main.pressure
        )
    }

    private fun containsForecastForCity(
        resultList: List<DatabaseModel>,
        cityName: String
    ): DatabaseModel? {
        for (model in resultList) {
            if (model.cityName.equals(cityName, true)) {
                return model
            }
        }
        return null
    }

}
