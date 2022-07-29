package com.bbj.myapplication.data

import android.content.Context
import com.bbj.myapplication.util.Constants
import com.bbj.myapplication.util.NameLanguage

class SharedPreferenceClient(context: Context) {

    private val sharedPreferences = context.getSharedPreferences(Constants.prefName, Context.MODE_PRIVATE)
    private val preferenceEditor = sharedPreferences.edit()

    fun getCityName(lang : NameLanguage) : String{
        return sharedPreferences.getString(Constants.prefCityKey,Constants.nurSultan)?.split("/")?.get(lang.ordinal)
            ?: Constants.nurSultan
    }


    fun setCityName(name : String){
        preferenceEditor.putString(Constants.prefCityKey,name).apply()
    }

}