package com.bbj.myapplication.view

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.bbj.myapplication.R
import com.bbj.myapplication.data.ForecastRepositoryImpl
import com.bbj.myapplication.data.WeatherModel
import com.bbj.myapplication.data.database.DatabaseClient
import com.bbj.myapplication.data.database.DatabaseDAO
import com.bbj.myapplication.data.database.ForecastDataBase
import com.bbj.myapplication.data.retrofit.RetrofitClient
import com.bbj.myapplication.data.retrofit.RetrofitDAO
import com.bbj.myapplication.domain.GettingTodayForecastUseCase
import com.bbj.myapplication.util.Constants
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel(application: Application) : AndroidViewModel(application) {
    
    init {
        Log.d("TAG","New instance of VM")
    }

    private var database: ForecastDataBase? = DatabaseClient.getDataBase(application)
    private var databaseDao: DatabaseDAO? = database?.getDAO()

    private val retrofit = RetrofitClient.getRetrofit()
    private val retrofitDao = retrofit.create(RetrofitDAO::class.java)

    private val forecastRepository: ForecastRepositoryImpl =
        ForecastRepositoryImpl(databaseDao!!, retrofitDao)

    private val getTodayForecastUseCase = GettingTodayForecastUseCase(forecastRepository)

    val prefs by lazy {application.getSharedPreferences(Constants.prefName,Context.MODE_PRIVATE)}

    private val _liveForecast = MutableLiveData<WeatherModel>()
    val liveForecast: LiveData<WeatherModel>
        get() = _liveForecast

    private val _liveGif = MutableLiveData<Int>()
    val liveGif: LiveData<Int>
        get() = _liveGif

    val rainGifArray = arrayListOf<Int>(R.drawable.rain_cat,R.drawable.rain_panda)
    val hotGifArray = arrayListOf<Int>(R.drawable.hot_anime_girl,R.drawable.hot_cat,R.drawable.hot_panda_2)
    val warmGifArray = arrayListOf<Int>(R.drawable.warm_panda_1,R.drawable.warm_panda_2,R.drawable.dance_unihorn,R.drawable.dance_unicorn_2)
    val coldGifArray = arrayListOf<Int>(R.drawable.cold_bugcat,R.drawable.cold_panda,R.drawable.cold_penguin)
    val random = Random()

    val coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable ->
        throwable.printStackTrace()
    }


    fun getTodayForecast() {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val weather = getTodayForecastUseCase.execute(prefs
                .getString(Constants.prefCityKey,Constants.nurSultan)!!
                .split("/")[0])
            Handler(Looper.getMainLooper()).post{
                _liveForecast.value = weather
                _liveGif.value = chooseGifAnim(weather)
            }
        }
    }

    fun chooseGifAnim(weatherModel: WeatherModel) : Int {
        return if (!weatherModel.description.contains("дожд")){
            when (weatherModel.temp.toInt()){
                in 0..14 -> coldGifArray[random.nextInt(coldGifArray.size)]
                in 15..24 -> warmGifArray[random.nextInt(warmGifArray.size)]
                in 24..55 -> hotGifArray[random.nextInt(hotGifArray.size)]
                else -> 0
            }
        } else {
            rainGifArray[random.nextInt(rainGifArray.size)]
        }
    }

}