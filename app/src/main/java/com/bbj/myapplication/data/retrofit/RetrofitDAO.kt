package com.bbj.myapplication.data.retrofit

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface RetrofitDAO {

    @GET("weather?units=metric&lang=ru")
    suspend fun getForecastFromWeb(@Query("q") cityName: String, @Query("appid") apiKey: String)
            : Response<WebForecastModel>

}