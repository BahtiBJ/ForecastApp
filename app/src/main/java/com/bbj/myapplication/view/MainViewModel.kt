package com.bbj.myapplication.view

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.*
import com.bbj.myapplication.R
import com.bbj.myapplication.data.SharedPreferenceClient
import com.bbj.myapplication.data.WeatherModel
import com.bbj.myapplication.util.NameLanguage
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel(application: Application) : AndroidViewModel(application) {
    
    private val getTodayForecastUseCase = (application as MainApplication).appComponent.getForecastUseCase()

    private val prefClient by lazy {SharedPreferenceClient(application)}

    private val _liveForecast = MutableLiveData<WeatherModel>()
    val liveForecast: LiveData<WeatherModel>
        get() = _liveForecast

    private val _liveGif = MutableLiveData<Int>()
    val liveGif: LiveData<Int>
        get() = _liveGif

    private val rainGifArray = arrayListOf<Int>(R.drawable.rain_cat,R.drawable.rain_panda)
    private val hotGifArray = arrayListOf<Int>(R.drawable.hot_anime_girl,R.drawable.hot_cat,R.drawable.hot_panda_2)
    private val warmGifArray = arrayListOf<Int>(R.drawable.warm_panda_1,R.drawable.warm_panda_2,R.drawable.dance_unihorn,R.drawable.dance_unicorn_2)
    private val coldGifArray = arrayListOf<Int>(R.drawable.cold_bugcat,R.drawable.cold_panda,R.drawable.cold_penguin)
    private val random = Random()

    private val coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable ->
        throwable.printStackTrace()
    }


    fun getTodayForecast() {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val weather = getTodayForecastUseCase.execute(prefClient.getCityName(NameLanguage.EN))
            val chosenGIF = chooseGifAnim(weather)
            Handler(Looper.getMainLooper()).post{
                _liveForecast.value = weather
                _liveGif.value = chosenGIF
            }
        }
    }

    private fun chooseGifAnim(weatherModel: WeatherModel) : Int {
        return  if (!weatherModel.description.contains("дожд",true)) {
            when (weatherModel.temp.toInt()) {
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