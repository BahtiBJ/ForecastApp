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
import javax.inject.Inject

class ForecastRepositoryImpl @Inject constructor
    (private val databaseDAO: DatabaseDAO, private val retrofitDAO: RetrofitDAO?) :
    ForecastRepository {

    private var apiKey : String = ""
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

    override fun setAPIKey(apiKey: String) {
        this.apiKey = apiKey
    }

    override suspend fun getTodayWeatherForecast(cityName: String): WeatherModel {
        val resultFromDB = databaseDAO.getByDate(today)
        val resultWeatherModel : WeatherModel
        Log.d("TAG", "CITYNAME === $cityName")
        if (resultFromDB.isEmpty()) {
            val weatherModel = getWeatherModelByRetrofit(cityName,apiKey)
            Log.d("TAG", "Result empty Rep get from web = ${weatherModel}")
            if (weatherModel.temp != 404.0) {
                databaseDAO.insertWithRefresh(DatabaseModel(today,cityName,weatherModel,0))
            }
            resultWeatherModel = weatherModel
        } else {
            val currentForecast = getForecastForCity(resultFromDB, cityName)
            if (currentForecast != null) {
                Log.d("TAG", "Rep get from db = ${currentForecast.weatherModel}")
                resultWeatherModel = currentForecast.weatherModel
            } else {
                val weatherModel = getWeatherModelByRetrofit(cityName,apiKey)
                Log.d("TAG", "Result not empty Rep get from web = ${weatherModel}")
                if (weatherModel.temp != 404.0) {
                    databaseDAO.insert(DatabaseModel(today,cityName, weatherModel,0))
                }
                resultWeatherModel = weatherModel
            }
        }
        return resultWeatherModel
    }

    //Get data from web
    private suspend fun getWeatherModelByRetrofit(cityName: String, apiKey : String): WeatherModel {
        val response = retrofitDAO?.getForecastFromWeb(cityName,apiKey)
        if (response!!.isSuccessful) {
            val resultFromWeb = response.body()
            delay(1500)
            val weatherModel = getWeatherModel(resultFromWeb!!)
            return weatherModel
        }
        return defaultWeatherModel
    }

    private fun getWeatherModel(webForecast: WebForecastModel): WeatherModel {
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

    //Search current city in list
    private fun getForecastForCity(
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
