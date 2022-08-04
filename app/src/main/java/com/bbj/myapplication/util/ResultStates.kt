package com.bbj.myapplication.util

import com.bbj.myapplication.data.WeatherModel

sealed class ResultStates {

    class Success(val weatherModel : WeatherModel) : ResultStates()

    class Error(val message : String) : ResultStates()

}