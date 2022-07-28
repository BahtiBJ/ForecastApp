package com.bbj.myapplication.data.retrofit

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface RetrofitDAO {

    @GET("weather?units=metric&appid=f86f377fe5e7e05ab27c252568e0d218&lang=ru")
    suspend fun getForecastFromWeb(@Query("q") cityName : String):Response<WebForecastModel>

}