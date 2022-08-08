package com.bbj.myapplication.data

import android.content.Context
import com.bbj.myapplication.util.NameLanguage

const val prefName = "default"
const val prefCityKey = "cityName"
const val nurSultan = "Nur-Sultan/Нур-Султан"
const val prefApiKey = "apiKey"

class SharedPreferenceClient(context: Context) {

    private val sharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
    private val preferenceEditor = sharedPreferences.edit()

    fun getCityName(lang : NameLanguage) : String{
        return sharedPreferences.getString(prefCityKey,nurSultan)?.split("/")?.get(lang.ordinal)
            ?: nurSultan
    }


    fun setCityName(name : String){
        preferenceEditor.putString(prefCityKey,name).apply()
    }

    fun getApiKey() : String{
        return sharedPreferences.getString(prefApiKey,"") ?: ""
    }


    fun setApiKey(apiKey : String){
        preferenceEditor.putString(prefApiKey,apiKey).apply()
    }

}