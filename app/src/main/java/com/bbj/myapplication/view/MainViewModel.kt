package com.bbj.myapplication.view

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.*
import com.bbj.myapplication.R
import com.bbj.myapplication.data.SharedPreferenceClient
import com.bbj.myapplication.data.WeatherModel
import com.bbj.myapplication.util.NameLanguage
import com.bbj.myapplication.util.NetworkCheck
import com.bbj.myapplication.util.ResultStates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val getTodayForecastUseCase = (application as MainApplication).appComponent.getForecastUseCase()

    private val prefClient by lazy {SharedPreferenceClient(application)}

    private val _liveForecast = MutableLiveData<ResultStates>()
    val liveForecast: LiveData<ResultStates>
        get() = _liveForecast

    private val _liveGif = MutableLiveData<Int>()
    val liveGif: LiveData<Int>
        get() = _liveGif

    private val rainGifArray = arrayListOf<Int>(R.drawable.rain_cat,R.drawable.rain_panda)
    private val hotGifArray = arrayListOf<Int>(R.drawable.hot_anime_girl,R.drawable.hot_cat,R.drawable.hot_panda_2)
    private val warmGifArray = arrayListOf<Int>(R.drawable.warm_panda_1,R.drawable.warm_panda_2,R.drawable.dance_unihorn,R.drawable.dance_unicorn_2)
    private val coldGifArray = arrayListOf<Int>(R.drawable.cold_bugcat,R.drawable.cold_panda,R.drawable.cold_penguin)
    private val random = Random()
    
    private var hasAPIKey : Boolean = false

    fun setAPIKey(apiKey : String){
        getTodayForecastUseCase.setAPIKey(apiKey)
        hasAPIKey = true
    }

    fun isAPIKeySetted() : Boolean{
        hasAPIKey = SharedPreferenceClient(getApplication()).getApiKey() != ""
        return hasAPIKey
    }

    fun getTodayForecast(necessaryUpdateGif : Boolean = true) {
        if (!hasAPIKey){
            _liveForecast.value = ResultStates.Error("API ключ не установлен")
            return
        }
        if (NetworkCheck.isOnline(getApplication())) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val weather =
                        getTodayForecastUseCase.execute(prefClient.getCityName(NameLanguage.EN))
                    val chosenGIF = chooseGifAnim(weather)
                    Handler(Looper.getMainLooper()).post {
                        _liveForecast.value = ResultStates.Success(weather)
                        if (necessaryUpdateGif)
                            _liveGif.value = chosenGIF
                    }
                } catch (e : Exception){
                    Handler(Looper.getMainLooper()).post {
                        _liveForecast.value = ResultStates.Error(e.localizedMessage
                            ?: "Неизвестная ошибка")
                    }
                }
            }
        } else {
            _liveForecast.value = ResultStates.Error("Не удалось получить данные из сети")
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