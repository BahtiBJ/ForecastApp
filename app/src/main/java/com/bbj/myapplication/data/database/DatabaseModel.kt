package com.bbj.myapplication.data.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.bbj.myapplication.data.WeatherModel

@Entity
data class DatabaseModel(val date : String,
                         val cityName : String,
                         @Embedded val weatherModel: WeatherModel,
                         @PrimaryKey(autoGenerate = true) val id : Int
)
