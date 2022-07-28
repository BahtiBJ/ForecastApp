package com.bbj.myapplication.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

object DatabaseClient {

    private var database : ForecastDataBase? = null

    fun getDataBase(context: Context) : ForecastDataBase{
        if (database == null){
            database = Room
                .databaseBuilder(context,ForecastDataBase::class.java,"database")
                .fallbackToDestructiveMigration()
                .build()
        }
        return database!!
    }
}