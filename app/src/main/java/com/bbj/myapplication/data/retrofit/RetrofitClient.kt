package com.bbj.myapplication.data.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient  {

    private var retrofit : Retrofit? = null
    private const val baseUrl = "https://api.openweathermap.org/data/2.5/"

    fun getRetrofit() : Retrofit{
        if (retrofit == null){
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }

}